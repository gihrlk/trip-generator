package net.codingchallenge.tripgenerator.exception;

public class TripGenerationException extends Exception {

	private static final long serialVersionUID = 5499819122371277198L;

	public TripGenerationException() {
		super("Error occured while generating the trip.");
	}

	public TripGenerationException(String message) {
		super(message);
	}
}
