package net.codingchallenge.tripgenerator.validator;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.codingchallenge.tripgenerator.exception.InputFileException;
import net.codingchallenge.tripgenerator.exception.OutputFileException;

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

	public void validateFilePaths(String inputFilePath, String outputFilePath) throws InputFileException, OutputFileException {

		// Validate input file
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists()) {
			throw new InputFileException("Can't find the input file.");
		} else if (!inputFile.isFile() || !isJsonFile(inputFilePath)) {
			throw new InputFileException("Input file is not a JSON file.");
		}

		// Validate output file
		File outputFile = new File(outputFilePath);
		File parentDirectory = outputFile.getParentFile();
		if (parentDirectory == null || !parentDirectory.exists() || !parentDirectory.isDirectory()) {
			throw new OutputFileException("Can't find the output directory. The directory must exists.");
		}
		if (!inputFile.isFile() || !isJsonFile(outputFilePath)) {
			throw new OutputFileException("Output file must have the json extention.");
		}
		if (outputFile.exists()) {
			logger.warn("File {} exists. It will be overridden.", () -> outputFilePath);
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
