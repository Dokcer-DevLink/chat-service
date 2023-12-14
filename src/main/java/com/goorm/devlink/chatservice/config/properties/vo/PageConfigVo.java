package com.example.chattingservice.config.properties.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class PageConfigVo {

    private final int offset;
    private final int size;
    private final String orderBy;

}
