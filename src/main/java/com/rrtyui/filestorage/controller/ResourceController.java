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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Resource controller", description = "A controller that deals with files and directories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource")
public class ResourceController {

    private final MinioFileStorageService minioFileStorageService;

    @Operation(
            summary = "Getting information about a files/directories by path"
    )
    @GetMapping()
    public ResponseEntity<?> infoAboutResource(@RequestParam("path") @Parameter(description = "Actually the path") String path) {
        List<MinioResponse> minioResponses = minioFileStorageService.getInfo(path);
        return ResponseEntity.ok(minioResponses);
    }

    @Operation(
            summary = "Deleting a files/directories by path"
    )
    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestParam("path") @Parameter(description = "Actually the path") String path) {
        minioFileStorageService.delete(path);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Download a files/directories by path. Directories download as ZIP-archive"
    )
    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam("path") @Parameter(description = "Actually the path") String path) {
        minioFileStorageService.download(path);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Renaming or moving files/directories from path \"FROM\" to path \"TO\""
    )
    @GetMapping("/move")
    public ResponseEntity<?> move(@RequestParam("from") @Parameter(description = "Start path") String from,
                                  @RequestParam("to") @Parameter(description = "End path") String to) {
        MinioResponse minioResponse = minioFileStorageService.move(from, to);
        return ResponseEntity.ok(minioResponse);
    }

    @Operation(
            summary = "Search by query parameter"
    )
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("query") @Parameter(description = "Search query") String query) {
        List<MinioResponse> searched = minioFileStorageService.search(query);

        return new ResponseEntity<>(searched, HttpStatus.OK);
    }

    @Operation(
            summary = "Upload format - MultipartFile"
    )
    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("path") @Parameter(description = "Actually the path") String path,
                                    @RequestParam("file")  @Parameter(description = "List of downloaded files") List<MultipartFile> file) {
        List<MinioResponse> minioResponses = minioFileStorageService.upload(file, path);
        return new ResponseEntity<>(minioResponses, HttpStatus.CREATED);
    }
}
