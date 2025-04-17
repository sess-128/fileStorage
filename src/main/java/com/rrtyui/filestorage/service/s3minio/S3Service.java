package com.rrtyui.filestorage.service.s3minio;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

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

    @SneakyThrows
    public void uploadJsonFile() {
        ObjectWriteResponse test = minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket(ROOT_BUCKET).object("my-object-name").filename("src/main/resources/person.json").build());

        System.out.println("Файл загружен" + test);
    }

//    @SneakyThrows
//    public void uploadVideoFile() {
//        minioClient.uploadObject(UploadObjectArgs.builder()
//                .bucket(ROOT_BUCKET).filename()
//    }

    @SneakyThrows
    public void createRootFolderForUser(Long id) {
        // Create object ends with '/' (also called as folder or directory).
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(ROOT_BUCKET).object("user-" + id + "-folder/").stream(
                                new ByteArrayInputStream(new byte[] {}), 0, -1)
                        .build());
    }
}
