package com.desafio.tds.urlshortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Objects;

@Configuration
public class MongoConfig {

    @Autowired
    private Environment env;

    @Bean
    public MongoDatabaseFactory mongodatabasefactory() {
        return new SimpleMongoClientDatabaseFactory(Objects.requireNonNull(env.getProperty("spring.data.mongodb.uri")));
    }

    @Bean
    public MongoTemplate mongoTemplate() {

        return new MongoTemplate(mongodatabasefactory());

    }
}
