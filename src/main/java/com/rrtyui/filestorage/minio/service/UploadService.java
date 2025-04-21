package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.security.MyUserDetails;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class UploadService extends BaseService{

    public UploadService(MinioUtils minioUtils, MinioRepository minioRepository) {
        super(minioUtils, minioRepository);
    }

    public void uploadFileOrFolder(MultipartFile file, String targetPath, MyUserDetails userDetails) {
        String userPrefix = minioUtils.getCurrentUserPath(userDetails);
        String fullPath = userPrefix + targetPath;

        if (file.getOriginalFilename() != null && file.getOriginalFilename().endsWith(".zip")) {
            uploadZipArchive(file, fullPath);
        } else {
            uploadSingleFile(file, fullPath);
        }
    }

    @SneakyThrows
    private void uploadSingleFile(MultipartFile file, String targetPath) {
        String objectName = targetPath + "/" + file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();

        try (InputStream inputStream = file.getInputStream()) {
            minioRepository.uploadFile(inputStream, objectName, contentType, size);
        }
    }

    @SneakyThrows
    private void uploadZipArchive(MultipartFile zipFile, String targetPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String objectName = targetPath + "/" + entry.getName();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }

                    try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
                        String contentType = minioUtils.getContentType(entry.getName());
                        int size = baos.size();
                        minioRepository.uploadZip(bais, size, objectName, contentType);
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }
}
