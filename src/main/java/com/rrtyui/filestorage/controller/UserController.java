package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.service.MyUserService;
import com.rrtyui.filestorage.util.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/me")
public class UserController {

    private final MyUserService userService;

    @GetMapping()
    public ResponseEntity<?> getMe(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        UserResponse userResponse = userService.getUserById(myUserDetails);
        return ResponseEntity.ok(userResponse);
    }
}
