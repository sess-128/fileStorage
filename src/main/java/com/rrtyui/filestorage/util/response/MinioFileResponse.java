package com.rrtyui.filestorage.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MinioFileResponse {
    private String path;
    private String name;
    private Long size;
    private String type;

}
