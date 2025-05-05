package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.minio.service.impl.MinioFileStorageService;
import com.rrtyui.filestorage.util.MinioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Directory controller", description = "A controller that deals directories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {

    private final MinioFileStorageService minioFileStorageService;

    @Operation(
            summary = "Create empty folder"
    )
    @PostMapping
    public ResponseEntity<?> createEmptyFolder(@RequestParam("path") @Parameter(description = "Create an empty folder at the specified path") String path) {
        MinioResponse minioResponse = minioFileStorageService.createEmptyFolder(path);

        return new ResponseEntity<>(minioResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Getting information about a directory by path"
    )
    @GetMapping
    public ResponseEntity<?> getDirectoryInfo(@RequestParam("path") String path) {
        List<MinioResponse> minioResponses = minioFileStorageService.getInfo(path);
        return ResponseEntity.ok(minioResponses);
    }
}
