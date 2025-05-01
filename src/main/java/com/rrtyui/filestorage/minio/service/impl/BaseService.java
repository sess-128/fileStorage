package com.rrtyui.filestorage.minio.service.impl;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseService {
    protected final MinioUtil minioUtil;
    protected final MinioRepository minioRepository;
}
