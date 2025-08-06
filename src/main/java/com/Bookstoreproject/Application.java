package com.Bookstoreproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
@SpringBootApplication

@EnableJpaRepositories(basePackages = "com.Bookstoreproject.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.Bookstoreproject.repository.elasticSearch")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
