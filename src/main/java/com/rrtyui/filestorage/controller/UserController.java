package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.service.MyUserService;
import com.rrtyui.filestorage.util.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/me")
public class UserController {

    private final MyUserService userService;

    @GetMapping()
    public ResponseEntity<?> getMe(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        MyUser user = userService.getUserById(myUserDetails);

        UserResponse userResponse = ResponseMapper.toUserResponse(user);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
