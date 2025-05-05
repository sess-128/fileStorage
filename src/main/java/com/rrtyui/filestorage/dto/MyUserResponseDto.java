package com.rrtyui.filestorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "User response DTO")
public class MyUserResponseDto {
    @Schema(description = "Username", example = "registered user")
    private String username;
}
