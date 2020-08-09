package com.lgh.demo.shardingdatabasetable.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JackSonDefaultConfiguration {

/*    @Bean
    public ObjectMapper objectMapper() {
*//*
        StdTypeResolverBuilder typeResolverBuilder = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
        typeResolverBuilder = typeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
*//*


        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setSerializationInclusion(JsonInclude.Include.NON_NULL);//.setDefaultTyping(typeResolverBuilder);

    }*/

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE).setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }
}

