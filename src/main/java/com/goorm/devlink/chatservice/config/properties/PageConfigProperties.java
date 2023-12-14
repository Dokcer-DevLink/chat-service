package com.example.chattingservice.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@ConfigurationProperties("data.page")
public class PageConfigProperties {

    @NotEmpty
    private int offset;
    @NotEmpty
    private int size;
    @NotEmpty
    private String orderBy;
}
