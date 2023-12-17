package com.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportsApplication {

	// TODO: el PDF no se genera con las fuentes correctas (ahora usa DejaVu Sans)
	public static void main(String[] args) {
		SpringApplication.run(ReportsApplication.class, args);
	}
}
