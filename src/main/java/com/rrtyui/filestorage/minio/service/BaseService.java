package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class BaseService {

    protected final MinioUtils minioUtils;
    protected final MinioRepository minioRepository;
}
