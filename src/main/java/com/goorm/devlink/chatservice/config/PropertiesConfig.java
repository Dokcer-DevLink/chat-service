package com.example.chattingservice.config;

import com.example.chattingservice.config.properties.KafkaConfigProperties;
import com.example.chattingservice.config.properties.PageConfigProperties;
import com.example.chattingservice.config.properties.StompConfigProperties;
import com.example.chattingservice.config.properties.vo.KafkaConfigVo;
import com.example.chattingservice.config.properties.vo.PageConfigVo;
import com.example.chattingservice.config.properties.vo.StompConfigVo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        KafkaConfigProperties.class, StompConfigProperties.class, PageConfigProperties.class
})
@RequiredArgsConstructor
public class PropertiesConfig {

    private final KafkaConfigProperties kafkaConfigProperties;
    private final StompConfigProperties stompConfigProperties;
    private final PageConfigProperties pageConfigProperties;

    @Bean
    public KafkaConfigVo kafkaConfigVo(){
        return new KafkaConfigVo(
                kafkaConfigProperties.getTopicName(),
                kafkaConfigProperties.getBootstrapServerUrl()
                );
    }

    @Bean
    public StompConfigVo stompConfigVo(){
        return new StompConfigVo(
          stompConfigProperties.getEndpoint(),
          stompConfigProperties.getSub(),
          stompConfigProperties.getPub()
        );
    }

    @Bean
    public PageConfigVo pageConfigVo(){
        return new PageConfigVo(
                pageConfigProperties.getOffset(),
                pageConfigProperties.getSize(),
                pageConfigProperties.getOrderBy()
        );
    }


}
