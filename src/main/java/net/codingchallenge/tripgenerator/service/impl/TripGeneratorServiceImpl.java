package net.codingchallenge.tripgenerator.service.impl;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.codingchallenge.tripgenerator.enums.TapType;
import net.codingchallenge.tripgenerator.enums.TripStatus;
import net.codingchallenge.tripgenerator.exception.TripGenerationException;
import net.codingchallenge.tripgenerator.model.Tap;
import net.codingchallenge.tripgenerator.model.TripCost;
import net.codingchallenge.tripgenerator.service.TripGeneratorService;
import net.codingchallenge.tripgenerator.store.TripCostStore;

/**
 * The TripGeneratorServiceImpl class provides implementation for core
 * capabilities require to generate a trip.
 * 
 * @author Gihan Rajakaruna
 *
 */
@Service("tripGeneratorService")
public class TripGeneratorServiceImpl implements TripGeneratorService {

	@Override
	public List<Tap> getAllTapOns(List<Tap> taps) {
		List<Tap> tapOnList = taps.stream().filter(tap -> tap.getTapType() == TapType.ON).collect(Collectors.toList());
		return tapOnList;
	}

	/**
	 * The OFF tap must be recorded after the ON tap and it should have the same
	 * account number, company id and bus id.
	 */
	@Override
	public Tap getTapOff(List<Tap> taps, Tap tapOn) {
		Optional<Tap> tapOffEntry = taps.stream()
				.filter(tap -> (tap.getTapType() == TapType.OFF
						&& tap.getPrimaryAccountNumber().equals(tapOn.getPrimaryAccountNumber())
						&& tap.getCompanyId().equals(tapOn.getCompanyId()) && tap.getBusId().equals(tapOn.getBusId())
						&& tap.getDatetimeUTC().isAfter(tapOn.getDatetimeUTC())))
				.findFirst();
		if (tapOffEntry != null && tapOffEntry.isPresent()) {
			return tapOffEntry.get();
		}
		return null;
	}

	@Override
	public long getTripDuration(ZonedDateTime tapOnTime, ZonedDateTime tapOffTime) throws TripGenerationException {
		if (tapOnTime.isAfter(tapOffTime)) {
			throw new TripGenerationException("Tap OFF time must be after tap ON time.");
		}

		long diffSeconds = ChronoUnit.SECONDS.between(tapOnTime, tapOffTime);
		return diffSeconds;
	}

	/**
	 * When there is no OFF bus stop, the maximum fare should be calculated.
	 */
	@Override
	public double getTripFare(String sourceBusStopId) throws TripGenerationException {
		TripCost tripCost = TripCostStore.getTripCostList().stream()
				.filter(cost -> sourceBusStopId.equals(cost.getSource())
						|| sourceBusStopId.equals(cost.getDestination()))
				.sorted((TripCost cost1, TripCost cost2) -> Double.compare(cost2.getCost(), cost1.getCost()))
				.findFirst().orElse(null);
		if (tripCost == null) {
			throw new TripGenerationException("Couldn't calculate the trip fare.");
		}
		return tripCost.getCost();
	}

	/**
	 * Calculates the fare between ON and OFF bus stops.
	 */
	@Override
	public double getTripFare(String sourceBusStopId, String destinationBusStopId) throws TripGenerationException {
		TripCost tripCost = TripCostStore.getTripCostList().stream().filter(
				cost -> (sourceBusStopId.equals(cost.getSource()) && destinationBusStopId.equals(cost.getDestination()))
						|| (destinationBusStopId.equals(cost.getSource()))
								&& sourceBusStopId.equals(cost.getDestination()))
				.sorted((TripCost cost1, TripCost cost2) -> Double.compare(cost2.getCost(), cost1.getCost())).findAny()
				.orElse(null);
		if (tripCost == null) {
			throw new TripGenerationException("Couldn't calculate the trip fare.");
		}
		return tripCost.getCost();
	}

	/**
	 * If OFF bus stop is null, then it's a INCOMPLETE trip. If both stops are the
	 * same, then it is a CANCELLED trip. Otherwise it's a COMPLETED trip.
	 */
	@Override
	public TripStatus getTripStatus(String sourceBusStopId, String destinationBusStopId)
			throws TripGenerationException {
		if (sourceBusStopId == null) {
			throw new TripGenerationException("From bus stop can't be null.");
		} else if (destinationBusStopId == null) {
			return TripStatus.INCOMPLETE;
		} else if (sourceBusStopId.equals(destinationBusStopId)) {
			return TripStatus.CANCELLED;
		}
		return TripStatus.COMPLETED;
	}
}
