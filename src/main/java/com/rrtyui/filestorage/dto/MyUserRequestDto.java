package com.rrtyui.filestorage.dto;

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
public class MyUserRequestDto {
    @NotEmpty(message = "НЕ пусто")
    @Size(min = 2, max = 100, message = "от 2 до 100")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password")
    private String password;
}
