package com.rrtyui.filestorage.eventandlistener;

import com.rrtyui.filestorage.minio.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredListener {
    private final MinioService minioService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        Long id = event.getId();
        minioService.createRootFolderForUser(id);
        System.out.println("Сделал папку для юзера с айди " + id);
    }
}
