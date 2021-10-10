package net.codingchallenge.tripgenerator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import net.codingchallenge.tripgenerator.controller.TripGeneratorController;
import net.codingchallenge.tripgenerator.service.TripGeneratorService;
import net.codingchallenge.tripgenerator.store.TripCostStore;

@ActiveProfiles("test")
@SpringBootTest
class TripGeneratorApplicationTest {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		// Make sure the application context is initialized properly
		assertNotNull(context.getBean(TripGeneratorController.class));
		assertNotNull(context.getBean(TripGeneratorService.class));
		assertNotNull(context.getBean(TripCostStore.class));
		assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(CommandLineTaskExecutor.class),
				"CommandLineRunner should not be loaded during this integration test");
	}
}
