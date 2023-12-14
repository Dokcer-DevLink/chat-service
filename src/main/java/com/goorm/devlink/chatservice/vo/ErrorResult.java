package com.example.chattingservice.vo;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ErrorResult {

    private String requestUrl;
    private LocalDateTime timestamp;
    private String errorMessage;
    private List<String> errorMessages;

    public static ErrorResult getInstance(String errorMessage, String requestUrl){
        return ErrorResult.builder()
                .timestamp(LocalDateTime.now())
                .errorMessage(errorMessage)
                .requestUrl(requestUrl)
                .build();
    }

    public static ErrorResult getInstance(List<String> errorMessages, String requestUrl){
        return ErrorResult.builder()
                .timestamp(LocalDateTime.now())
                .errorMessages(errorMessages)
                .requestUrl(requestUrl)
                .build();
    }
}
