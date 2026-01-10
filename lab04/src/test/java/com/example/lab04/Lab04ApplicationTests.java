package com.example.lab04;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Skipping default integration test in CI environment")
class Lab04ApplicationTests {

	@Test
	void contextLoads() {
	}

}
