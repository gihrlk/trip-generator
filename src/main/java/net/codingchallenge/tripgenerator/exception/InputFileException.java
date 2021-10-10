package net.codingchallenge.tripgenerator.exception;

public class InputFileException extends Exception {

	private static final long serialVersionUID = -2577706144438847189L;

	public InputFileException() {
		super("Error occured while processing the input file.");
	}

	public InputFileException(String message) {
		super(message);
	}
}
