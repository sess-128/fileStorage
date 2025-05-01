package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.minio.service.impl.MinioFileStorageService;
import com.rrtyui.filestorage.util.MinioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {

    private final MinioFileStorageService minioFileStorageService;

    @PostMapping
    public ResponseEntity<?> createEmptyFolder(@RequestParam("path") String path) {
        MinioResponse minioResponse = minioFileStorageService.createEmptyFolder(path);

        return new ResponseEntity<>(minioResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getDirectoryInfo(@RequestParam("path") String path) {
        List<MinioResponse> minioResponses = minioFileStorageService.getInfo(path);
        return ResponseEntity.ok(minioResponses);
    }
}
