package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.security.MyUserDetails;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class UploadService extends BaseService{

    public UploadService(MinioUtils minioUtils, MinioRepository minioRepository) {
        super(minioUtils, minioRepository);
    }

    public void uploadFileOrFolder(List<MultipartFile> files, String targetPath, MyUserDetails userDetails) {
        String userPrefix = minioUtils.getCurrentUserPath(userDetails);
        String fullBasePath = userPrefix + targetPath;

//        if (minioRepository.isResourceExist(fullBasePath)) {
//            throw new UserAlreadyExistException("Файл или папка уже существует");
//        }
//        if (file.getOriginalFilename() != null && file.getOriginalFilename().endsWith(".zip")) {
//            uploadZipArchive(file, fullBasePath);
//        } else {
//            uploadSingleFile(file, fullBasePath);
//        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String relativePath = file.getOriginalFilename();
            String fullPath = fullBasePath + "/" + relativePath;

            uploadSingleFile(file, fullPath);
        }
    }

    @SneakyThrows
    private void uploadSingleFile(MultipartFile file, String targetPath) {
        String contentType = file.getContentType();
        long size = file.getSize();

        try (InputStream inputStream = file.getInputStream()) {
            minioRepository.uploadFile(inputStream, targetPath, contentType, size);
        }
    }
}
