package com.probase.fra.farmerspay.api.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.probase.fra.farmerspay.api.deserializers.TimestampDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class JacksonConfiguration {
    @Bean
    public ObjectMapper objectMapper() {

        //        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addDeserializer(LocalDateTime.class, new TimestampDeserializer());
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
//                .registerModule(javaTimeModule)
                .registerModule(new JavaTimeModule());

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new TimestampDeserializer());
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .addModule(javaTimeModule)
                .build();
    }
}
