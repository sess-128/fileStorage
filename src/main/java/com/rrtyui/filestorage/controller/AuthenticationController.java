package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.exception.UserNotCreatedException;
import com.rrtyui.filestorage.service.AuthService;
import com.rrtyui.filestorage.util.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<UserResponse> registration(@RequestBody @Valid MyUserRequestDto myUserRequestDto,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                errorMsg.append(fieldError.getField())
                        .append(" - ")
                        .append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }

        authService.register(myUserRequestDto);
        UserResponse response = new UserResponse(myUserRequestDto.getName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<UserResponse> auth(@RequestBody @Valid MyUserRequestDto myUserRequestDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                errorMsg.append(fieldError.getField())
                        .append(" - ")
                        .append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        authService.signIn(myUserRequestDto);

        UserResponse response = new UserResponse(myUserRequestDto.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
