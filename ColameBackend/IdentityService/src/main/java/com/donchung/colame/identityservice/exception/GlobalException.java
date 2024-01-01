package com.donchung.colame.identityservice.exception;

import com.donchung.colame.commonservice.utils.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handlerResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<Object> baseResponse = ApiResponse
                .builder()
                .code(String.format(HttpStatus.NOT_FOUND.toString()))
                .data(ex.getLocalizedMessage())
                .success(false).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }
}
