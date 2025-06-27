package com.tibbertil.botforbibyt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BotforbibytApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotforbibytApplication.class, args);
	}

}
