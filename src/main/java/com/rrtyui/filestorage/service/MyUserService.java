package com.rrtyui.filestorage.service;

import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.exception.UserNotFoundException;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.repository.UserRepository;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.dto.MyUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserService {

    private final UserRepository userRepository;

    public MyUserResponseDto getUserById(MyUserDetails myUserDetails) {
        Long id = myUserDetails.getMyUser().getId();
        MyUser myUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: '" + id + "' doesn't exist"));
        return ResponseMapper.toUserResponse(myUser);
    }
}
