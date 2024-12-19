package com.punna.eventcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class EventCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventCatalogApplication.class, args);
	}

}
