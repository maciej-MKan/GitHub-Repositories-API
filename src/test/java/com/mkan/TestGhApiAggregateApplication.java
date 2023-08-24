package com.mkan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGhApiAggregateApplication {

	public static void main(String[] args) {
		SpringApplication.from(GhApiAggregateApplication::main).with(TestGhApiAggregateApplication.class).run(args);
	}

}
