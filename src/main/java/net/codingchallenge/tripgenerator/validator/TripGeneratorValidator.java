package net.codingchallenge.tripgenerator.validator;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.codingchallenge.tripgenerator.exception.InputFileException;

/**
 * The TripGeneratorValidator class performs basic validations.
 * 
 * @author Gihan Rajakaruna
 *
 */
@Component
public class TripGeneratorValidator {

	final static Logger logger = LogManager.getLogger(TripGeneratorValidator.class);

	final static String FILE_EXTENTION = "json";

	public void validateFilePaths(String inputFilePath, String outputFilePath) throws InputFileException {

		// Validate input file
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists()) {
			throw new InputFileException("Can't find the input file.");
		} else if (!inputFile.isFile() || !isJsonFile(inputFilePath)) {
			throw new InputFileException("Input file is not a JSON file.");
		}

		// Validate output file
		File outputFile = new File(outputFilePath);
		if (outputFile.exists()) {
			logger.debug("File {} exists. It will be overriden.", () -> outputFilePath);
		}
		if (!inputFile.isFile() || !isJsonFile(outputFilePath)) {
			throw new InputFileException("Output file is not a JSON file.");
		}
	}

	boolean isJsonFile(String filePath) {
		String fileExtention = FilenameUtils.getExtension(filePath);
		logger.debug("File {} has the extension {}", () -> filePath, () -> fileExtention);
		if (FILE_EXTENTION.equalsIgnoreCase(fileExtention)) {
			return true;
		}
		return false;
	}
}
