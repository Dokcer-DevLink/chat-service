package com.example.chattingservice.config;

import com.example.chattingservice.config.properties.vo.KafkaConfigVo;
import com.example.chattingservice.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaConfigVo kafkaConfigVo;

    // Producer Config
    @Bean
    public ProducerFactory<String, ChatDto> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs(),null,new JsonSerializer<ChatDto>());
    }

    @Bean
    public KafkaTemplate<String, ChatDto> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public Map<String,Object> producerConfigs() {
        Map<String,Object> configurations = new HashMap<>();
        configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConfigVo.getBootstrapServerUrl());
        configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configurations.put(CommonClientConfigs.GROUP_ID_CONFIG,KafkaConfigVo.GROUP_ID);
        return configurations;
    }

    //Listener Config
    @Bean
    public ConsumerFactory<String,ChatDto> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),null,new JsonDeserializer<>(ChatDto.class,false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,ChatDto> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, ChatDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public Map<String,Object> consumerConfigs(){
        Map<String,Object> configurations = new HashMap<>();
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConfigVo.getBootstrapServerUrl());
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configurations.put(CommonClientConfigs.GROUP_ID_CONFIG,KafkaConfigVo.GROUP_ID);
        configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");// ??
        return configurations;

    }

}
