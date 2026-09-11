package com.middleware.apitv.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

	@Value("${mongodb.user}")
    private String user;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.cluster}")
    private String cluster;

    @Value("${mongodb.database}")
    private String database;
      
    @Bean
    public MongoTemplate mongoTemplate() {
    	String connectionString = String.format(
                "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority",
                user, password, cluster, database
            );

        MongoClient mongoClient = MongoClients.create(connectionString);
        SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, database);
        
        return new MongoTemplate(factory);
    }
}

