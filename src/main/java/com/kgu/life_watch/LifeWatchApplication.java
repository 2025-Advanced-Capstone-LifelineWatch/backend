package com.kgu.life_watch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LifeWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeWatchApplication.class, args);
	}

}
