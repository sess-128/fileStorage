package com.rrtyui.filestorage.service;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.eventandlistener.UserRegisteredEvent;
import com.rrtyui.filestorage.exception.UserAlreadyExistException;
import com.rrtyui.filestorage.mapper.MyUserMapper;
import com.rrtyui.filestorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final MyUserMapper myUserMapper;

    @Transactional
    public void register(MyUserRequestDto myUserRequestDto) {
        if (userRepository.existsByUsername(myUserRequestDto.getUsername())) {
            throw new UserAlreadyExistException("User with this name is exist, please select another one");
        }

        MyUser myUser = myUserMapper.toMyUser(myUserRequestDto);

        userRepository.saveAndFlush(myUser);

        eventPublisher.publishEvent(new UserRegisteredEvent(this, myUser.getId()));
    }
}
