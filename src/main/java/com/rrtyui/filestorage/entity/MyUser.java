package com.rrtyui.filestorage.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "users")
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "НЕ пусто")
    @Size(min = 2, max = 100, message = "от 2 до 100")
    @Column(name = "username", unique = true, nullable = false)
    private String name;

    @Column(name = "password")
    private String password;
}
