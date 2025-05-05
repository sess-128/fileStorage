package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.exception.UserAlreadyExistException;
import com.rrtyui.filestorage.exception.UserNotFoundException;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.service.impl.BaseService;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import com.rrtyui.filestorage.util.MinioResponse;
import org.springframework.stereotype.Service;

@Service
public class CreateService extends BaseService {

    public CreateService(MinioUtil minioUtil, MinioRepository minioRepository) {
        super(minioUtil, minioRepository);
    }

    public void createRootFolderForUser(Long id) {
        String userRootDirectory = "user-" + id + "-files/";
        minioRepository.createRootFolderForUser(userRootDirectory);
    }

    public MinioResponse createEmptyFolder(String path) {
        minioUtil.validatePath(path);
        String finalFolderPath = minioUtil.getCurrentUserPath() + path;
        String parentPrefix = minioUtil.getParentPrefix(finalFolderPath);
        String userPrefix = minioUtil.getCurrentUserPath();

        if (!parentPrefix.isEmpty() && !parentPrefix.equals(userPrefix)) {
            if (!minioRepository.isResourceExist(parentPrefix)) {
                throw new UserNotFoundException("Родительской папки нет");
            }
        }

        if (minioRepository.isResourceExist(finalFolderPath)) {
            throw new UserAlreadyExistException("Файл или папка уже существует");
        }
        minioRepository.createEmptyDirectory(finalFolderPath);
        return ResponseMapper.toMinioResponse(path, minioUtil);

    }
}
