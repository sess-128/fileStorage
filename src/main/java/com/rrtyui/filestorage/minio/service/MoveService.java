package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.exception.UserAlreadyExistException;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.service.impl.BaseService;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import com.rrtyui.filestorage.util.MinioResponse;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveService extends BaseService {

    private final DeleteService deleteService;
    private final InfoService infoService;

    @Autowired
    public MoveService(MinioUtil minioUtil, MinioRepository minioRepository, DeleteService deleteService, InfoService infoService) {
        super(minioUtil, minioRepository);
        this.deleteService = deleteService;
        this.infoService = infoService;
    }

    @SneakyThrows
    public MinioResponse move(String from, String to) {
        String fullFromPath = minioUtil.getCurrentUserPath() + from;
        String fullToPath = minioUtil.getCurrentUserPath() + to;

        if (minioRepository.isResourceExist(fullToPath)) {
            throw new UserAlreadyExistException("Файл или папка уже существует");
        }

        minioUtil.validateMove(fullFromPath, fullToPath);

        if (minioUtil.isDirectoryPath(from)) {
            String destination = minioUtil.defineOperation(from, to);
            return moveDirectory(from, destination);
        } else {
            String destination = minioUtil.defineOperation(from, to);
            return moveFile(from, destination);
        }
    }

    @SneakyThrows
    private MinioResponse moveDirectory(String from, String to) {
        String sourcePrefix = minioUtil.getCurrentUserPath() + from;
        String targetPrefix = minioUtil.getCurrentUserPath() + to;

        var objects = minioRepository.getContentsDirectoryRecursively(sourcePrefix);

        for (Result<Item> result : objects) {
            Item item = result.get();
            String name = item.objectName();
            String relativePath = name.substring(sourcePrefix.length());
            String newObjectName = targetPrefix + relativePath;

            minioRepository.moveFile(name, newObjectName);
        }
        deleteService.delete(from);
        return ResponseMapper.toMinioResponse(to, minioUtil);
    }

    @SneakyThrows
    private MinioResponse moveFile(String from, String to) {
        String fromSource = minioUtil.getCurrentUserPath() + from;
        String toSource = minioUtil.getCurrentUserPath() + to;

        minioRepository.moveFile(fromSource, toSource);
        deleteService.delete(from);

        return ResponseMapper.toMinioResponse(to, minioUtil);
    }
}
