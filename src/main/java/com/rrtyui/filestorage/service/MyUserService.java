package com.rrtyui.filestorage.service;

import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.exception.UserNotFoundException;
import com.rrtyui.filestorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserService {

    private final UserRepository userRepository;

    public MyUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: '" + id + "' doesn't exist"));
    }
}
