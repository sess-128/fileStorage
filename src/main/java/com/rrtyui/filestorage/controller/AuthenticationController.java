package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.exception.CollectorBindingResultError;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.service.AuthService;
import com.rrtyui.filestorage.service.RegisterService;
import com.rrtyui.filestorage.util.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;
    private final RegisterService registerService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> registration(@RequestBody @Valid MyUserRequestDto myUserRequestDto,
                                                     BindingResult bindingResult) {
        CollectorBindingResultError.throwIfHasErrors(bindingResult);

        registerService.register(myUserRequestDto);

        UserResponse userResponse = ResponseMapper.toUserResponse(myUserRequestDto);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponse> auth(@RequestBody @Valid MyUserRequestDto myUserRequestDto,
                                             BindingResult bindingResult) {
        CollectorBindingResultError.throwIfHasErrors(bindingResult);

        authService.signIn(myUserRequestDto);

        UserResponse userResponse = ResponseMapper.toUserResponse(myUserRequestDto);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
