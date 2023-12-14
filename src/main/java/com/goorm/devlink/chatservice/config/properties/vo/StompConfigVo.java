package com.example.chattingservice.config.properties.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class StompConfigVo {

    private final String endpoint;
    private final String sub;
    private final String pub;
}
