package net.codingchallenge.tripgenerator.exception;

public class OutputFileException extends Exception {

	private static final long serialVersionUID = -3253150668429820659L;

	public OutputFileException() {
		super("Error occurred while processing the output file.");
	}

	public OutputFileException(String message) {
		super(message);
	}
}
