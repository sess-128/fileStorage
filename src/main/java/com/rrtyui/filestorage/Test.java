package com.rrtyui.filestorage;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Test {
    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("hesaro_kan", "y768ayxxxL!")
                .build();

        ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("user-files").object("like-folder-2/inner/inner2/").stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());

        System.out.println(objectWriteResponse);
    }
}
