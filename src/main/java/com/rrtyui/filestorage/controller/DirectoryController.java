package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.minio.service.MinioService;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.util.response.MinioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {

    private final MinioService minioService;

    @PostMapping
    public ResponseEntity<?> createEmptyFolder(@RequestParam("path") String path,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        MinioResponse minioResponse = minioService.createEmptyFolder(path, myUserDetails);

        return new ResponseEntity<>(minioResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getDirectoryInfo(@RequestParam("path") String path,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        List<MinioResponse> minioResponses = minioService.directoryInfo(path, myUserDetails);

        return new ResponseEntity<>(minioResponses, HttpStatus.OK);
    }
}
