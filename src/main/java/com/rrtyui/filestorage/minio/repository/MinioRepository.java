package com.rrtyui.filestorage.minio.repository;

import com.rrtyui.filestorage.exception.PathNotFoundException;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Repository
@RequiredArgsConstructor
public class MinioRepository {
    private static final String ROOT_BUCKET = "user-files";

    private final MinioClient minio;

    public StatObjectResponse getResourceInfo(String name) {
        try {
            return minio.statObject(
                    StatObjectArgs.builder()
                            .bucket(ROOT_BUCKET)
                            .object(name)
                            .build());
        } catch (Exception e) {
            throw new PathNotFoundException("Path not found");
        }
    }

    public void deleteResource(String name) {
        try {
            minio.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(ROOT_BUCKET)
                            .object(name)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void createRootFolderForUser(String userDirectory) {
        minio.putObject(
                PutObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(userDirectory).stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());

    }

    @SneakyThrows
    public void createEmptyDirectory(String finalFolderPath) {
        minio.putObject(
                PutObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(finalFolderPath).stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }

    public Iterable<Result<Item>> getContentsDirectoryRecursively(String directory) {
        return minio.listObjects(
                ListObjectsArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .prefix(directory)
                        .recursive(true)
                        .build());
    }

    public Iterable<Result<Item>> getContentsDirectory(String sourcePrefix) {
        return minio.listObjects(
                ListObjectsArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .prefix(sourcePrefix)
                        .delimiter("/")
                        .recursive(false)
                        .build());
    }

    @SneakyThrows
    public InputStream getStreamForDownload(String name) {
        return minio.getObject(
                GetObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(name)
                        .build()
        );
    }

    @SneakyThrows
    public void deleteFile(String toRemove) {
        minio.removeObject(RemoveObjectArgs.builder()
                .bucket(ROOT_BUCKET)
                .object(toRemove)
                .build());
    }

    private CopySource getCopySource(String fromSource) {
        return CopySource.builder()
                .bucket(ROOT_BUCKET)
                .object(fromSource)
                .build();
    }

    @SneakyThrows
    public void moveFile(String fromSource, String toSource) {
        minio.copyObject(
                CopyObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(toSource)
                        .source(
                                getCopySource(fromSource)
                        )
                        .build());
    }


    @SneakyThrows
    public void uploadFile(InputStream inputStream, String objectName, String contentType, long size) {
        minio.putObject(
                PutObjectArgs.builder()
                        .bucket(ROOT_BUCKET)
                        .object(objectName)
                        .stream(inputStream, size, -1)
                        .contentType(contentType)
                        .build()
        );
    }

    public boolean isResourceExist(String path) {
        try {
            StatObjectResponse resourceInfo = minio.statObject(
                    StatObjectArgs.builder()
                            .bucket(ROOT_BUCKET)
                            .object(path)
                            .build()
            );
            return !resourceInfo.object().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

}
