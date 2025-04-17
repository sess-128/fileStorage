package com.rrtyui.filestorage.eventandlistener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    private final Long id;
    public UserRegisteredEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
