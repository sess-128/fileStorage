package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.exception.CollectorBindingResultError;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.service.AuthService;
import com.rrtyui.filestorage.service.RegisterService;
import com.rrtyui.filestorage.util.response.UserResponse;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;
    private final RegisterService registerService;
    private final HttpServletRequest httpServletRequest;

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
        System.out.println("Session ID: " + httpServletRequest.getSession().getId());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/test-session")
    public String testSession(@AuthenticationPrincipal MyUserDetails userDetails, HttpServletRequest request) {
        return "User: " + (userDetails != null ? userDetails.getUsername() : "null")
                + ", Session ID: " + request.getSession().getId();
    }

}
