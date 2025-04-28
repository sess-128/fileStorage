package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.exception.UserAlreadyExistException;
import com.rrtyui.filestorage.exception.UserNotFoundException;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.util.response.MinioResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class CreateService extends BaseService{

    public CreateService(MinioUtils minioUtils, MinioRepository minioRepository) {
        super(minioUtils, minioRepository);
    }

    @SneakyThrows
    public void createRootFolderForUser(Long id) {
        String userRootDirectory = "user-" + id + "-files/";
        minioRepository.createRootFolderForUser(userRootDirectory);
    }

    @SneakyThrows
    public MinioResponse createEmptyFolder(String path, MyUserDetails userDetails) {
        String finalFolderPath = minioUtils.getCurrentUserPath(userDetails) + path;
        String parentPrefix = minioUtils.getParentPrefix(finalFolderPath);
        String userPrefix = minioUtils.getCurrentUserPath(userDetails);

        if (!parentPrefix.isEmpty() && !parentPrefix.equals(userPrefix)) {
            if (!minioRepository.isResourceExist(parentPrefix)) {
                throw new UserNotFoundException("Родительской папки нет");
            }
        }

        if (minioRepository.isResourceExist(finalFolderPath)) {
            throw new UserAlreadyExistException("Файл или папка уже существует");
        }
        minioRepository.createEmptyDirectory(finalFolderPath);
        return ResponseMapper.toMinioResponse(path);

    }
}
