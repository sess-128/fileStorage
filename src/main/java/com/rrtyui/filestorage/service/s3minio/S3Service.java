package com.rrtyui.filestorage.service.s3minio;

import com.rrtyui.filestorage.entity.MyUser;

import com.rrtyui.filestorage.security.MyUserDetails;
import io.minio.*;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Bucket - имя бакета, созданного админом - мной, в котором и сохраняются файлы и/или папки каждого пользователя
 * object - то, как будет называться загруженный файл
 * filename - расположение загружаемого файла
 */

@Service
@RequiredArgsConstructor
public class S3Service {
    private static final String ROOT_BUCKET = "user-files";

    private final MinioClient minioClient;
    private final HttpServletResponse httpServletResponse;

    @SneakyThrows
    public StatObjectResponse getResourceInfo(String path, MyUserDetails userDetails) {
        //TODO: Валидатор, который определяет файл это или папка по окончании строки на "/".
        //TODO: Также нужен класс, который разбивает путь. Еще непонятно как катчить эксепшены с минио
        return minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(
                                getCurrentUserPath(userDetails) + path
                        )
                        .build());
    }

    @SneakyThrows
    public void deleteResource(String path, MyUserDetails userDetails) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(getCurrentUserPath(userDetails) + path)
                        .build());
    }

    @SneakyThrows
    public void createRootFolderForUser(Long id) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(ROOT_BUCKET).object("user-" + id + "-files/").stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }

    @SneakyThrows
    public void downloadResource(String path, MyUserDetails userDetails) {
        //TODO: квалификатор папка скачивается или файл, например метод, который запускает разные методы: скачивание файла/папки
        if (path.endsWith("/")) {
            downloadFolder(path, userDetails);
        } else {
            downloadFile(path, userDetails);
        }
    }

    @SneakyThrows
    public void downloadFolder(String path, MyUserDetails userDetails) {
        Iterable<Result<Item>> files = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .prefix(getCurrentUserPath(userDetails) + path)
                        .recursive(true)
                        .build()
        );

        String zipName = createZipName(path);
        httpServletResponse.setContentType("application/zip");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + zipName + "\"");


        ZipOutputStream zipOutputStream = new ZipOutputStream(httpServletResponse.getOutputStream());
        for (Result<Item> file : files) {
            Item item = file.get();
            if (!item.isDir()) {
                String objectName = item.objectName();
                String entryName = objectName.substring(zipName.length());

                ZipEntry zipEntry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(zipEntry);

                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(ROOT_BUCKET)
                                .object(objectName)
                                .build()
                );
                IOUtils.copy(inputStream, zipOutputStream);
            }
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();

    }

    @SneakyThrows
    public void downloadFile(String path, MyUserDetails MyUserDetails) {
        String fileName = createFileName(path);
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(getCurrentUserPath(MyUserDetails) + path)
                        .build()
        );
        IOUtils.copy(stream, httpServletResponse.getOutputStream());
    }

    private String getCurrentUserPath(MyUserDetails MyUserDetails) {
        return  "user-%s-files/".formatted(MyUserDetails.getMyUser().getId());
    }

    private String createFileName(String path) {
        path = path.replaceAll("/+$", "");
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String createZipName(String path) {
        return createFileName(path) + ".zip";
    }
}
