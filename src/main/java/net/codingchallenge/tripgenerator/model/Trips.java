package net.codingchallenge.tripgenerator.model;

import java.util.List;

/**
 * The Trips class wraps list of trip objects. It is used when marshalling the
 * JSON output.
 * 
 * @author Gihan Rajakaruna
 *
 */
public class Trips {

	List<Trip> trips;

	public List<Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}
}
