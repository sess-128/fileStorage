package com.rrtyui.filestorage.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rrtyui.filestorage.minio.util.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@Builder
public class MinioResponse {
    private String path;
    private String name;
    private Long size;
    private ResourceType type;
}
