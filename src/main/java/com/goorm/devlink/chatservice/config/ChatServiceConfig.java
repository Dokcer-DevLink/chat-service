package com.example.chattingservice.config;


import com.example.chattingservice.util.MessageUtil;
import com.example.chattingservice.util.ModelMapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatServiceConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public ModelMapperUtil modelMapperUtil(){
        return new ModelMapperUtil(modelMapper());
    }

    @Bean
    public MessageUtil messageUtil(MessageSource messageSource){ return new MessageUtil(messageSource); }
}
