package com.mk.bettinggenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mk.bettinggenerator.util.MapDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register custom deserializer
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, new MapDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
