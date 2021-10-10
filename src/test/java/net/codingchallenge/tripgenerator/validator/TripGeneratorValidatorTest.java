package net.codingchallenge.tripgenerator.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import net.codingchallenge.tripgenerator.exception.InputFileException;
import net.codingchallenge.tripgenerator.exception.OutputFileException;

/**
 * The TripGeneratorValidatorTest performs unit tests on validation methods.
 * 
 * @author Gihan Rajakaruna
 *
 */
@ActiveProfiles("test")
@SpringBootTest
public class TripGeneratorValidatorTest {

	@Autowired
	private TripGeneratorValidator tripGeneratorValidator;

	@Test
	void validateFilePathsTest1() throws InputFileException, OutputFileException {
		tripGeneratorValidator.validateFilePaths("src/main/resources/tests/input/tap-data-1.json",
				"src/main/resources/tests/output/trip-data-1.json");
	}

	@Test
	void validateFilePathsTest2() {
		InputFileException thrownException = assertThrows(InputFileException.class,
				() -> tripGeneratorValidator.validateFilePaths("src/main/resources/tests/input/tap-data-1",
						"src/main/resources/tests/output/trip-data-1.json"),
				"Expected to get an exception, but it didn't");

		assertTrue(thrownException.getMessage().contains("Can't find the input file."));
	}

	@Test
	void validateFilePathsTest3() {
		InputFileException thrownException = assertThrows(InputFileException.class,
				() -> tripGeneratorValidator.validateFilePaths("src/main/resources/tests/input",
						"src/main/resources/tests/output/trip-data-1.json"),
				"Expected to get an exception, but it didn't");

		assertTrue(thrownException.getMessage().contains("Input file is not a JSON file."));
	}

	@Test
	void validateFilePathsTest4() {
		OutputFileException thrownException = assertThrows(OutputFileException.class,
				() -> tripGeneratorValidator.validateFilePaths("src/main/resources/tests/input/tap-data-1.json",
						"src/main/resources/tests/output/trip-data-1"),
				"Expected to get an exception, but it didn't");

		assertTrue(thrownException.getMessage().contains("Output file is must have the json extention."));
	}

}
