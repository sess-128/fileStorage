package com.rrtyui.filestorage.mapper;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.entity.MyUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyUserMapper {

    private final PasswordEncoder passwordEncoder;

    public MyUser toMyUser (MyUserRequestDto dto) {
        return MyUser.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }
}
