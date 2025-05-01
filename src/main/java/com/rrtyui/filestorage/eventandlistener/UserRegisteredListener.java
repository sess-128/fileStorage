package com.rrtyui.filestorage.eventandlistener;

import com.rrtyui.filestorage.minio.service.impl.MinioFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredListener {
    private final MinioFileStorageService minioFileStorageService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        Long id = event.getId();
        minioFileStorageService.createRootFolderByUserId(id);
    }
}
