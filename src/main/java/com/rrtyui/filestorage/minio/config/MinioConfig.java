package com.rrtyui.filestorage.minio.config;

import com.rrtyui.filestorage.minio.util.MinioUtils;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://minioS3:9000")
                .credentials("local_user", "y768ayxxxL!")
                .build();
    }

    @Bean
    public MinioUtils minioUtils() {
        return new MinioUtils();
    }
}
