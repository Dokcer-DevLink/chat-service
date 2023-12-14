package com.example.chattingservice.exception;


import com.example.chattingservice.vo.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ServiceExceptionAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResult> methodNoSuchElementExceptionHandler(NoSuchElementException exception,
                                                                           HttpServletRequest request){
        return new ResponseEntity<>(ErrorResult.getInstance(exception.getMessage(),
                request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }
}
