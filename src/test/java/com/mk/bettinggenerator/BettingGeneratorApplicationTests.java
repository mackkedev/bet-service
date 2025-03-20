package com.mk.bettinggenerator;

import com.mk.bettinggenerator.controller.BettingController;
import com.mk.bettinggenerator.service.BettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.springframework.test.util.AssertionErrors.assertNotNull;


@SpringBootTest
class BettingGeneratorApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	BettingService bettingService;

	@Autowired
	BettingController bettingController;
	@Test
	void contextLoads() {
		assertNotNull("Application context", context);
		assertNotNull("betting service initialized", bettingService);
	}

}
