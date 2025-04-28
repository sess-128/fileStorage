package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.minio.service.MinioMainService;
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

    private final MinioMainService minioMainService;

    @PostMapping
    public ResponseEntity<?> createEmptyFolder(@RequestParam("path") String path,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        MinioResponse minioResponse = minioMainService.createEmptyFolder(path, myUserDetails);

        return new ResponseEntity<>(minioResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getDirectoryInfo(@RequestParam("path") String path,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        List<MinioResponse> minioResponses = minioMainService.directoryInfo(path, myUserDetails);

        return new ResponseEntity<>(minioResponses, HttpStatus.OK);
    }
}
