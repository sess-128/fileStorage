package com.rrtyui.filestorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User request DTO")
public class MyUserRequestDto {
    @NotEmpty(message = "НЕ пусто")
    @Size(min = 2, max = 100, message = "от 2 до 100")
    @Column(name = "username", unique = true, nullable = false)
    @Schema(description = "Username", example = "new_user_login")
    private String username;

    @Column(name = "password")
    @Schema(description = "Password", example = "user_password")
    private String password;
}
