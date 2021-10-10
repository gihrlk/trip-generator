package net.codingchallenge.tripgenerator.model;

import java.util.List;

/**
 * The Taps class wraps list of tap objects. It is used when unmarshalling the
 * JSON input.
 * 
 * @author Gihan Rajakaruna
 *
 */
public class Taps {

	List<Tap> taps;

	public List<Tap> getTaps() {
		return taps;
	}

	public void setTaps(List<Tap> taps) {
		this.taps = taps;
	}

}
