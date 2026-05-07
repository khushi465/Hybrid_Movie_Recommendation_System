package com.Khushi.recommendation_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

@EnableCaching
@EnableKafka
@EnableKafkaRetryTopic
@SpringBootApplication
public class RecommendationBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecommendationBackendApplication.class, args);
	}

}
