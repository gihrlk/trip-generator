package net.codingchallenge.tripgenerator.service;

import java.time.ZonedDateTime;
import java.util.List;

import net.codingchallenge.tripgenerator.enums.TripStatus;
import net.codingchallenge.tripgenerator.exception.TripGenerationException;
import net.codingchallenge.tripgenerator.model.Tap;

/**
 * The TripGeneratorService interface provides the core capabilities require to
 * generate a trip.
 * 
 * @author Gihan Rajakaruna
 *
 */
public interface TripGeneratorService {

	/**
	 * getAllTapOns method finds all ON taps in input list.
	 * 
	 * @param taps
	 * @return list of ON tap's
	 */
	List<Tap> getAllTapOns(List<Tap> taps);

	/**
	 * getTapOff method finds corresponding OFF tap for a ON tap. If no such tap is
	 * found it will return null.
	 * 
	 * @param taps
	 * @param tapOn
	 * @return OFF tap
	 */
	Tap getTapOff(List<Tap> taps, Tap tapOn);

	/**
	 * getTripDuration method calculates the duration between tap ON and OFF times
	 * in seconds.
	 * 
	 * @param tapOnTime
	 * @param tapOffTime
	 * @return number of seconds
	 * @throws TripGenerationException
	 */
	long getTripDuration(ZonedDateTime tapOnTime, ZonedDateTime tapOffTime) throws TripGenerationException;

	/**
	 * getTripFare method calculates the maximum fare from a bus stop.
	 * 
	 * @param sourceBusStopId
	 * @return trip fare
	 * @throws TripGenerationException
	 */
	double getTripFare(String sourceBusStopId) throws TripGenerationException;

	/**
	 * getTripFare method calculates the fare between two bus stops.
	 * 
	 * @param sourceBusStopId
	 * @param destinationBusStopId
	 * @return trip fare
	 * @throws TripGenerationException
	 */
	double getTripFare(String sourceBusStopId, String destinationBusStopId) throws TripGenerationException;

	/**
	 * getTripStatus methods returns the trip status by looking at from and to bus
	 * stops.
	 * 
	 * @param sourceBusStopId
	 * @param destinationBusStopId
	 * @return trip status
	 * @throws TripGenerationException
	 */
	TripStatus getTripStatus(String sourceBusStopId, String destinationBusStopId) throws TripGenerationException;
}
