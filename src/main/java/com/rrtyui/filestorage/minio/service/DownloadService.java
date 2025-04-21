package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.security.MyUserDetails;
import io.minio.Result;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DownloadService extends BaseService {

    private final HttpServletResponse httpServletResponse;

    public DownloadService(MinioUtils minioUtils, MinioRepository minioRepository, HttpServletResponse httpServletResponse) {
        super(minioUtils, minioRepository);
        this.httpServletResponse = httpServletResponse;
    }

    @SneakyThrows
    public void downloadResource(String path, MyUserDetails userDetails) {
        if (minioUtils.isDirectoryPath(path)) {
            downloadDirectory(path, userDetails);
        } else {
            downloadFile(path, userDetails);
        }
    }

    @SneakyThrows
    public void downloadDirectory(String path, MyUserDetails userDetails) {
        String prefix = minioUtils.getCurrentUserPath(userDetails) + path;
        Iterable<Result<Item>> objects = minioRepository.getContentsDirectoryRecursively(prefix);

        String zipName = minioUtils.createZipName(path);
        httpServletResponse.setContentType("application/zip");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + zipName + "\"");


        ZipOutputStream zipOutputStream = new ZipOutputStream(httpServletResponse.getOutputStream());
        for (Result<Item> file : objects) {
            Item item = file.get();
            if (!item.isDir()) {
                String objectName = item.objectName();
                String entryName = objectName.substring(zipName.length());

                ZipEntry zipEntry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(zipEntry);

                InputStream stream = minioRepository.getStreamForDownload(objectName);
                IOUtils.copy(stream, zipOutputStream);
            }
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
    }

    @SneakyThrows
    public void downloadFile(String path, MyUserDetails myUserDetails) {
        String fileName = minioUtils.createFileName(path);
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        String name = minioUtils.getCurrentUserPath(myUserDetails) + path;
        InputStream stream = minioRepository.getStreamForDownload(name);
        IOUtils.copy(stream, httpServletResponse.getOutputStream());
    }
}
