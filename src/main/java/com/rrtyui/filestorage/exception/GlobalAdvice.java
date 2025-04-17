package com.rrtyui.filestorage.exception;

import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "User with this id not found"
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotCreatedException e) {
        ErrorResponse response = ResponseMapper.toErrorResponse(e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserAlreadyExistException e) {
        ErrorResponse response = ResponseMapper.toErrorResponse(e);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadCredentialsException e) {
        ErrorResponse response = ResponseMapper.toErrorResponse(e);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401
    }
}
