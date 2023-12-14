package com.example.chattingservice.exception;


import com.example.chattingservice.util.MessageUtil;
import com.example.chattingservice.vo.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.chattingservice.controller")
@RequiredArgsConstructor
public class ControllerExceptionAdvice {

    private final MessageUtil messageUtil;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> methodArgumentExHandler(MethodArgumentNotValidException ex,
                                                               HttpServletRequest request){
        return new ResponseEntity<>(ErrorResult.getInstance(getMethodArgumentNotValidMessage(ex),
                request.getRequestURL().toString()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResult> MissingServletRequestParameterExceptionHandler(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResult.getInstance(ex.getMessage(), request.getRequestURL().toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResult> DateTimeParseExceptionHandler(DateTimeParseException ex,
                                                                     HttpServletRequest request){
        return new ResponseEntity<>(
                ErrorResult.getInstance(messageUtil.getNoIsoTypeMessage(),request.getRequestURL().toString())
                ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResult> MissingRequestHeaderExceptionHandler(MissingRequestHeaderException ex,
                                                                            HttpServletRequest request){
        return new ResponseEntity<>(ErrorResult.getInstance(ex.getMessage(),request.getRequestURL().toString()),
                HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResult> ConstraintViolationExceptionHandler(ConstraintViolationException ex,
                                                                           HttpServletRequest request){

        return new ResponseEntity<>(ErrorResult.getInstance(ex.getMessage(),request.getRequestURL().toString()),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> IllegalArgumentExceptionHandler(IllegalArgumentException exception,
                                                                       HttpServletRequest request){
        return new ResponseEntity<>(ErrorResult.getInstance(exception.getMessage(),request.getRequestURL().toString())
                ,HttpStatus.BAD_REQUEST);
    }

    private List<String> getMethodArgumentNotValidMessage(MethodArgumentNotValidException ex){
        ArrayList<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessages.add("[" + fieldError.getField() +"]는(은) " + fieldError.getDefaultMessage() +
                    " [ 입력된 값 : " + fieldError.getRejectedValue() + " ]");
        }
        return errorMessages;
    }



}
