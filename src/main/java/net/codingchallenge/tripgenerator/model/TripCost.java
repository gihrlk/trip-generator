package net.codingchallenge.tripgenerator.model;

/**
 * The TripCost class is used with TripCostStore to store trip cost details.
 * 
 * @author Gihan Rajakaruna
 *
 */
public class TripCost {

	private String source;

	private String destination;

	private double cost;

	public TripCost() {

	}

	public TripCost(String source, String destination, double cost) {
		this.source = source;
		this.destination = destination;
		this.cost = cost;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "TripCost [source=" + source + ", destination=" + destination + ", cost=" + cost + "]";
	}
}
