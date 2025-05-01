package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.service.impl.BaseService;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import com.rrtyui.filestorage.minio.util.WebUtil;
import io.minio.Result;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DownloadService extends BaseService {

    private final WebUtil webUtil;

    @Autowired
    public DownloadService(MinioUtil minioUtil, MinioRepository minioRepository, WebUtil webUtil) {
        super(minioUtil, minioRepository);
        this.webUtil = webUtil;
    }

    @SneakyThrows
    public void download(String path) {
        boolean isDirectory = minioUtil.isDirectoryPath(path);


        if (isDirectory) {
            String zipName = minioUtil.createZipName(path);
            HttpServletResponse response = webUtil.getPreparedResponse(true, zipName);
            downloadDirectory(path, response);
        } else {
            String fileName = minioUtil.createFileName(path);
            HttpServletResponse response = webUtil.getPreparedResponse(false, fileName);
            downloadFile(path, response);
        }
    }

    @SneakyThrows
    private void downloadDirectory(String path, HttpServletResponse httpServletResponse) {
        String prefix = minioUtil.getCurrentUserPath() + path;
        Iterable<Result<Item>> objects = minioRepository.getContentsDirectoryRecursively(prefix);

        ZipOutputStream zipOutputStream = new ZipOutputStream(httpServletResponse.getOutputStream());
        for (Result<Item> file : objects) {
            Item item = file.get();
            if (!item.isDir()) {
                String objectName = item.objectName();
                String entryName = objectName.substring(prefix.length());

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
    private void downloadFile(String path, HttpServletResponse httpServletResponse) {
        String name = minioUtil.getCurrentUserPath() + path;
        InputStream stream = minioRepository.getStreamForDownload(name);
        IOUtils.copy(stream, httpServletResponse.getOutputStream());
        stream.close();
    }
}
