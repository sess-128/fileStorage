package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.service.s3minio.S3Service;
import com.rrtyui.filestorage.util.response.MinioFileResponse;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource")
public class DirectoryController {

    private final S3Service s3Service;

    @GetMapping()
    public ResponseEntity<?> infoAboutResource(@RequestParam("path") String path,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        StatObjectResponse resourceInfo = s3Service.getResourceInfo(path, myUserDetails);

        MinioFileResponse minioFileResponse = MinioFileResponse.builder()
                .path(path)
                .name("Будущее имя")
                .size(resourceInfo.size())
                .type("Файл")
                .build();
        return new ResponseEntity<>(minioFileResponse, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteResource(@RequestParam("path") String path,
                                            @AuthenticationPrincipal MyUserDetails myUserDetails) {
        s3Service.deleteResource(path, myUserDetails);

        MinioFileResponse minioFileResponse = MinioFileResponse.builder()
                .path(path)
                .name("Удалили файл")
                .build();
        return new ResponseEntity<>(minioFileResponse, HttpStatus.OK);
    }

    @GetMapping("/download")
    public void download(@RequestParam("path") String path,
                         @AuthenticationPrincipal MyUserDetails myUserDetails) {
        s3Service.downloadResource(path, myUserDetails);
        MinioFileResponse minioFileResponse = MinioFileResponse.builder()
                .path(path)
                .name("Скачали файл")
                .build();
//        return new ResponseEntity<>(minioFileResponse, HttpStatus.OK);
    }


}
