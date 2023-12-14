package com.example.chattingservice.config.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.validation.constraints.NotEmpty;

@Slf4j
@Getter @Setter
@ConfigurationProperties("data.kafka")
public class KafkaConfigProperties {

    @NotEmpty
    private String topicName;
    @NotEmpty
    private String bootstrapServerUrl;


}
