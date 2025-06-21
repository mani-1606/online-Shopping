package com.javaboy.mani.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopConfig {
    /**
     * Creates and configures a ModelMapper instance for DTO conversions.
     * 
     * Note: ModelMapper 3.0.0 uses some deprecated internal JDK APIs.
     * These warnings can be suppressed using JVM argument: 
     * -Djdk.internal.lambda.disableEagerInitialization=true
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configure with strict mapping for more predictable behavior
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldMatchingEnabled(true)
            .setSkipNullEnabled(true);

        return modelMapper;
    }
}
