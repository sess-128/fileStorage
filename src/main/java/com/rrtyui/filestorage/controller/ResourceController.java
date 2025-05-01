package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.minio.service.impl.MinioFileStorageService;
import com.rrtyui.filestorage.util.MinioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource")
public class ResourceController {

    private final MinioFileStorageService minioFileStorageService;

    @GetMapping()
    public ResponseEntity<?> infoAboutResource(@RequestParam("path") String path) {
        List<MinioResponse> minioResponses = minioFileStorageService.getInfo(path);
        return ResponseEntity.ok(minioResponses);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteResource(@RequestParam("path") String path) {
        minioFileStorageService.delete(path);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam("path") String path) {
        minioFileStorageService.download(path);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/move")
    public ResponseEntity<?> move(@RequestParam("from") String from,
                                  @RequestParam("to") String to) {
        MinioResponse minioResponse = minioFileStorageService.move(from, to);
        return ResponseEntity.ok(minioResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("query") String query) {
        List<MinioResponse> searched = minioFileStorageService.search(query);

        return new ResponseEntity<>(searched, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("path") String path,
                                    @RequestParam("file") List<MultipartFile> file) {
        List<MinioResponse> minioResponses = minioFileStorageService.upload(file, path);
        return new ResponseEntity<>(minioResponses, HttpStatus.CREATED);
    }
}
