package net.codingchallenge.tripgenerator.store;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import net.codingchallenge.tripgenerator.model.TripCost;

/**
 * The TripCostStore class populates a list with cost to travel between two bus
 * stops.
 * 
 * @author Gihan Rajakaruna
 *
 */
@Component
public class TripCostStore {

	private static List<TripCost> tripCostList = new ArrayList<TripCost>();

	/**
	 * addCosts method should be executed after creating the bean.
	 */
	@PostConstruct
	public void addCosts() {
		tripCostList.add(new TripCost("Stop1", "Stop2", 3.25));
		tripCostList.add(new TripCost("Stop2", "Stop3", 5.50));
		tripCostList.add(new TripCost("Stop1", "Stop3", 7.30));
	}

	public static List<TripCost> getTripCostList() {
		return tripCostList;
	}

	public static void setTripCostList(List<TripCost> tripCostList) {
		TripCostStore.tripCostList = tripCostList;
	}
}
