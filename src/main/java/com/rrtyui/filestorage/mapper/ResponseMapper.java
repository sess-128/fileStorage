package com.rrtyui.filestorage.mapper;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import com.rrtyui.filestorage.minio.util.ResourceType;
import com.rrtyui.filestorage.util.ErrorResponse;
import com.rrtyui.filestorage.util.MinioResponse;
import com.rrtyui.filestorage.dto.MyUserResponseDto;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

public class ResponseMapper {

    public static MyUserResponseDto toUserResponse(MyUserRequestDto dto) {
        return new MyUserResponseDto(dto.getUsername());
    }

    public static MyUserResponseDto toUserResponse(MyUser user) {
        return new MyUserResponseDto(user.getUsername());
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
    public static MinioResponse toMinioResponse(String path, MinioUtil minioUtil) {
        boolean isDirectory = minioUtil.isDirectoryPath(path);

        MinioResponse.MinioResponseBuilder builder = MinioResponse.builder();

        builder.path(minioUtil.getParentPrefix(path));

        if (isDirectory) {
            builder.name(minioUtil.getLastSegmentPrefix(path));
            builder.type(ResourceType.DIRECTORY);
        } else {
            builder.name(minioUtil.createFileName(path));
            builder.type(ResourceType.FILE);
        }

        return builder.build();
    }

    public static MinioResponse toMinioResponse(Item item, String relativePath, String parentPath, MinioUtil minioUtil) {
        boolean isDirectory = item.isDir() || minioUtil.isDirectoryPath(item.objectName());

        MinioResponse.MinioResponseBuilder builder = MinioResponse.builder()
                .path(parentPath);

        if (isDirectory) {
            builder.name(minioUtil.getLastSegmentPrefix(relativePath));
            builder.type(ResourceType.DIRECTORY);
        } else {
            builder.name(minioUtil.createFileName(relativePath));
            builder.type(ResourceType.FILE);
            builder.size(item.size());
        }

        return builder.build();
    }

    public static MinioResponse toMinioResponse(MultipartFile file, String relativePath, MinioUtil minioUtil) {
        boolean isDirectory = minioUtil.isDirectoryPath(file.getOriginalFilename());

        MinioResponse.MinioResponseBuilder builder = MinioResponse.builder();

        builder.path(minioUtil.getParentPrefix(relativePath));
        if (isDirectory) {
            builder.name(minioUtil.getLastSegmentPrefix(relativePath));
            builder.type(ResourceType.DIRECTORY);
        } else {
            builder.name(minioUtil.createFileName(relativePath));
            builder.type(ResourceType.FILE);
            builder.size(file.getSize());
        }

        return builder.build();
    }
}
