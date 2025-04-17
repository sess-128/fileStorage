package com.rrtyui.filestorage.eventandlistener;

import com.rrtyui.filestorage.service.s3minio.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredListener {
    private final S3Service s3Service;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        Long id = event.getId();
        s3Service.createRootFolderForUser(id);
        System.out.println("Сделал папку для юзера с айди " + id);
    }
}
