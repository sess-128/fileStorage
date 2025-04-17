package com.rrtyui.filestorage.mapper;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.util.ErrorResponse;
import com.rrtyui.filestorage.util.UserResponse;

public class ResponseMapper {

    public static UserResponse toUserResponse(MyUserRequestDto dto) {
        return new UserResponse(dto.getName());
    }

    public static ErrorResponse toErrorResponse(Exception e){
        return new ErrorResponse(e.getMessage());
    }
}
