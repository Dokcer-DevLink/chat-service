package com.example.chattingservice.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@ConfigurationProperties("data.stomp")
public class StompConfigProperties {

    @NotEmpty
    private String endpoint;
    @NotEmpty
    private String sub;
    @NotEmpty
    private String pub;

}
