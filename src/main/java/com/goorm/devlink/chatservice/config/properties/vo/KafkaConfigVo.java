package com.example.chattingservice.config.properties.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class KafkaConfigVo {

    private final String topicName;
    private final String bootstrapServerUrl;
    public static final String GROUP_ID = "#{ T(java.util.UUID).randomUUID().toString() }";



}
