package com.rrtyui.filestorage.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class CollectorBindingResultError {
    public static void throwIfHasErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(" - ")
                        .append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }
    }
}
