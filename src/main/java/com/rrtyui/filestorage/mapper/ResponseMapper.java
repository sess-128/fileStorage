package com.rrtyui.filestorage.mapper;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.minio.util.ResourceType;
import com.rrtyui.filestorage.util.response.ErrorResponse;
import com.rrtyui.filestorage.util.response.MinioResponse;
import com.rrtyui.filestorage.util.response.UserResponse;
import io.minio.StatObjectResponse;

public class ResponseMapper {

    public static UserResponse toUserResponse(MyUserRequestDto dto) {
        return new UserResponse(dto.getUsername());
    }

    public static UserResponse toUserResponse(MyUser user) {
        return new UserResponse(user.getUsername());
    }

    public static ErrorResponse toErrorResponse(Exception e){
        return new ErrorResponse(e.getMessage());
    }

    public static MinioResponse toMinioResponse(StatObjectResponse response, String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        String lastSegment = path.substring(lastSlashIndex + 1);
        return MinioResponse.builder()
                .path(path)
                .name(lastSegment)
                .size(response.size())
                .type(ResourceType.FILE)
                .build();
    }
    public static MinioResponse toMinioResponse(String path) {
        String normalizedPath = path.endsWith("/")
                ? path.substring(0, path.length() - 1)
                : path;

        int lastSlashIndex = normalizedPath.lastIndexOf('/');

        String parentPath = lastSlashIndex >= 0
                ? normalizedPath.substring(0, lastSlashIndex + 1)
                : "";

        String folderName = normalizedPath.substring(lastSlashIndex + 1) + "/";

        return MinioResponse.builder()
                .path(parentPath)
                .name(folderName)
                .size(0L)
                .type(ResourceType.DIRECTORY)
                .build();
    }
}
