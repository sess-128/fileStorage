package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.minio.service.MinioMainService;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.util.response.MinioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource")
public class ResourceController {

    private final MinioMainService minioMainService;

    @GetMapping()
    public ResponseEntity<?> infoAboutResource(@RequestParam("path") String path,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        MinioResponse minioResponse = minioMainService.getResourceInfo(path, myUserDetails);

        return new ResponseEntity<>(minioResponse, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteResource(@RequestParam("path") String path,
                                            @AuthenticationPrincipal MyUserDetails myUserDetails) {
        minioMainService.deleteResource(path, myUserDetails);

        MinioResponse minioResponse = MinioResponse.builder()
                .path(path)
                .name("Удалили файл")
                .build();
        return new ResponseEntity<>(minioResponse, HttpStatus.OK);
    }

    @GetMapping("/download")
    public void download(@RequestParam("path") String path,
                         @AuthenticationPrincipal MyUserDetails myUserDetails) {
        minioMainService.downloadResource(path, myUserDetails);
        MinioResponse minioResponse = MinioResponse.builder()
                .path(path)
                .name("Скачали файл")
                .build();
//        return new ResponseEntity<>(minioFileResponse, HttpStatus.OK);
    }

    @GetMapping("/move")
    public void move(@RequestParam("from") String from,
                     @RequestParam("to") String to,
                     @AuthenticationPrincipal MyUserDetails myUserDetails) {
        System.out.println("Received from: " + from);
        System.out.println("Received to: " + to);
        minioMainService.renameOrMoveFile(from, to, myUserDetails);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("query") String query,
                                    @AuthenticationPrincipal MyUserDetails myUserDetails) {
        List<MinioResponse> searched = minioMainService.search(query, myUserDetails);

        return new ResponseEntity<>(searched, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("path") String path,
                                    @RequestParam("file") List<MultipartFile> file,
                                    @AuthenticationPrincipal MyUserDetails myUserDetails) {
        minioMainService.uploadFileOrFolder(file, path, myUserDetails);
        MinioResponse minioResponse = MinioResponse.builder()
                .name("Загружено что-то")
                .build();
        return new ResponseEntity<>(minioResponse, HttpStatus.CREATED);
    }
}
