package com.rrtyui.filestorage.mapper;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.entity.MyUser;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MyUserMapper {

    public MyUser toMyUser (MyUserRequestDto dto) {
        return MyUser.builder()
                .name(dto.getName())
                .password(dto.getPassword())
                .build();
    }
}
