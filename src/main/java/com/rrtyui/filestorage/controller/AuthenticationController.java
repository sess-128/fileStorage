package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.exception.CollectorBindingResultError;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.service.AuthService;
import com.rrtyui.filestorage.service.RegisterService;
import com.rrtyui.filestorage.dto.MyUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sign in and sign up controller", description = "Registers and authenticates users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;
    private final RegisterService registerService;

    @Operation(
            summary = "User registration",
            description = "Allows you to register a user"
    )
    @PostMapping("/sign-up")
    public ResponseEntity<?> registration(@RequestBody @Valid @Parameter(description = "User DTO")
                                          MyUserRequestDto myUserRequestDto,
                                          BindingResult bindingResult) {
        CollectorBindingResultError.throwIfHasErrors(bindingResult);

        registerService.register(myUserRequestDto);

        MyUserResponseDto myUserResponseDto = ResponseMapper.toUserResponse(myUserRequestDto);

        return new ResponseEntity<>(myUserResponseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "User authentication",
            description = "Authentication via login and password"
    )
    @PostMapping("/sign-in")
    public ResponseEntity<?> auth(@RequestBody @Valid @Parameter(description = "User DTO")
                                  MyUserRequestDto myUserRequestDto,
                                  BindingResult bindingResult) {
        CollectorBindingResultError.throwIfHasErrors(bindingResult);

        authService.signIn(myUserRequestDto);

        MyUserResponseDto myUserResponseDto = ResponseMapper.toUserResponse(myUserRequestDto);
        return new ResponseEntity<>(myUserResponseDto, HttpStatus.OK);
    }
}
