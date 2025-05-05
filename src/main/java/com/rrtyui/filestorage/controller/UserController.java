package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.service.MyUserService;
import com.rrtyui.filestorage.dto.MyUserResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User controller", description = "Allows you to get the authenticated user")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/me")
public class UserController {

    private final MyUserService userService;

    @GetMapping()
    public ResponseEntity<?> getMe(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        MyUserResponseDto myUserResponseDto = userService.getUserById(myUserDetails);
        return ResponseEntity.ok(myUserResponseDto);
    }
}
