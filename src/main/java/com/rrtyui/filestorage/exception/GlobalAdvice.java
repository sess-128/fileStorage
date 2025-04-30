package com.rrtyui.filestorage.exception;

import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.util.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        ErrorResponse response = ResponseMapper.toErrorResponse(e);

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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PathNotFoundException e) {
        ErrorResponse response = ResponseMapper.toErrorResponse(e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(InvalidPathException e) {
        ErrorResponse response = ResponseMapper.toErrorResponse(e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UnsupportedOperationException e) {
        ErrorResponse response = ResponseMapper.toErrorResponse(e);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409
    }





    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace(); // чтобы точно увидеть в консоли
        return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
    }
}
