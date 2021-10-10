package net.codingchallenge.tripgenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.codingchallenge.tripgenerator.controller.TripGeneratorController;

/**
 * The CommandLineTaskExecutor class implements CommandLineRunner. It is
 * initialized by SpringBoot after the application context is created. This
 * class is also the user entry point of the application. The run method will
 * get the user inputs and execute the generateTripsFromTaps method of the
 * controller. We don't want to initialize the CommandLineTaskExecutor during
 * integration tests and therefore we use a different profile to notify
 * SpringBoot that this class shouldn't be initialized when running
 * SpringBootTests.
 * 
 * @author Gihan Rajakaruna
 *
 */
@Profile("!test")
@Component
public class CommandLineTaskExecutor implements CommandLineRunner {

	final static Logger logger = LogManager.getLogger(TripGeneratorController.class);

	@Autowired(required = true)
	TripGeneratorController tripGeneratorController;

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 2) {
			tripGeneratorController.generateTripsFromTaps(args[0], args[1]);
		} else {
			logger.error("Please provide the input and output file paths.");
		}
	}
}
