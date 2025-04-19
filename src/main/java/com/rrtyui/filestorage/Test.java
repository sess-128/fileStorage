package com.rrtyui.filestorage;

import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Test {
    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("local_user", "y768ayxxxL!")
                .build();

        StatObjectResponse statted = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket("user-files")
                        .object("user-4-folder/2.png")
                        .build());

    }
}
