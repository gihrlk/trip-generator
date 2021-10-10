package net.codingchallenge.tripgenerator.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.codingchallenge.tripgenerator.exception.InputFileException;
import net.codingchallenge.tripgenerator.exception.OutputFileException;
import net.codingchallenge.tripgenerator.exception.TripGenerationException;
import net.codingchallenge.tripgenerator.model.Tap;
import net.codingchallenge.tripgenerator.model.Taps;
import net.codingchallenge.tripgenerator.model.Trip;
import net.codingchallenge.tripgenerator.model.Trips;
import net.codingchallenge.tripgenerator.service.TripGeneratorService;

/**
 * The TripGeneratorController class provides capability need to read input
 * taps, generate the trips and write the output.
 * 
 * @author Gihan Rajakaruna
 *
 */
@Controller
public class TripGeneratorController {

	final static Logger logger = LogManager.getLogger(TripGeneratorController.class);

	@Autowired(required = true)
	TripGeneratorService tripGeneratorService;

	/**
	 * generateTripsFromTaps method reads taps data from the input file and generate
	 * the trips output.
	 * 
	 * @param inputFilePath
	 * @param outputFilePath
	 */
	public void generateTripsFromTaps(String inputFilePath, String outputFilePath) {
		logger.debug("Reading Tap data from input file: {}", () -> inputFilePath);
		try {
			List<Tap> taps = readTapsFromInputFile(inputFilePath);
			if (taps != null && !taps.isEmpty()) {
				logger.debug("Input file contains {} taps. Processing...", () -> taps.size());
				List<Trip> trips = generateTripsFromTaps(taps);
				if (trips != null && !trips.isEmpty()) {
					logger.debug("{} trip(s) generated from input data. Saving the data to output file...",
							() -> trips.size());
					writeTripsToOutputFile(trips, outputFilePath);
					logger.debug("Successfully saved trip data to output file: {}", () -> outputFilePath);
				} else {
					logger.debug("No trips were generated. Nothing to output into file. Please check the input data.");
				}
			} else {
				logger.debug("No taps were found in input data. Please check the input data.");
			}
		} catch (TripGenerationException e) {
			logger.error("Error occured while generating trips from tap data.", e);
		} catch (InputFileException e) {
			logger.error("Error occured while reading the tap data.", e);
		} catch (OutputFileException e) {
			logger.error("Error occured while saving trip data.", e);
		}
	}

	/**
	 * generateTripsFromTaps method uses the service class to generate list of trips
	 * from the taps input.
	 * 
	 * @param taps
	 * @return list of trip's
	 * @throws TripGenerationException
	 * @throws TripFareException
	 */
	List<Trip> generateTripsFromTaps(List<Tap> taps) throws TripGenerationException {
		List<Trip> trips = new ArrayList<Trip>();
		// Get all tap ON's from the tap list
		List<Tap> tapOnList = tripGeneratorService.getAllTapOns(taps);
		if (tapOnList != null && !tapOnList.isEmpty()) {
			// Look for corresponding tap OFF for each tap ON and generate trip
			for (Tap tapOn : tapOnList) {
				logger.debug("Tap Id {} is a ON tap. Now looking for corresponding OFF tap.",
						() -> tapOn.getId());
				Tap tapOff = tripGeneratorService.getTapOff(taps, tapOn);
				Trip trip = generateTripFromTap(tapOn, tapOff);
				logger.debug("Successfully generated trip. {}", () -> trip.toString());
				trips.add(trip);
			}
		} else {
			logger.debug("Input file doesn't have any ON tap. Please check the input data.");
		}
		return trips;
	}

	/**
	 * generateTripFromTap method accepts two tap objects and generates a trip using
	 * the service class.
	 * 
	 * @param tapOn
	 * @param tapOff
	 * @return trip
	 * @throws TripGenerationException
	 * @throws TripFareException
	 */
	Trip generateTripFromTap(Tap tapOn, Tap tapOff) throws TripGenerationException {
		Trip trip = new Trip();
		if (tapOff != null) {
			logger.debug("Tap Id {} is the OFF tap of Tap Id {}. Generating the trip...", () -> tapOff.getId(),
					() -> tapOn.getId());
			trip.setStarted(tapOn.getDatetimeUTC());
			trip.setFinished(tapOff.getDatetimeUTC());
			trip.setDurationSecs(tripGeneratorService.getTripDuration(tapOn.getDatetimeUTC(), tapOff.getDatetimeUTC()));
			trip.setFromStopId(tapOn.getStopId());
			trip.setToStopId(tapOff.getStopId());
			trip.setChargeAmount(tripGeneratorService.getTripFare(tapOn.getStopId(), tapOff.getStopId()));
			trip.setCompanyId(tapOn.getCompanyId());
			trip.setBusId(tapOn.getBusId());
			trip.setPrimaryAccountNumber(tapOn.getPrimaryAccountNumber());
			trip.setStatus(tripGeneratorService.getTripStatus(tapOn.getStopId(), tapOff.getStopId()));
		} else {
			logger.debug("Tap Id {} doesn't have a OFF tap. Generating the trip with max fare...",
					() -> tapOn.getId());
			trip.setStarted(tapOn.getDatetimeUTC());
			trip.setFromStopId(tapOn.getStopId());
			trip.setChargeAmount(tripGeneratorService.getTripFare(tapOn.getStopId()));
			trip.setCompanyId(tapOn.getCompanyId());
			trip.setBusId(tapOn.getBusId());
			trip.setPrimaryAccountNumber(tapOn.getPrimaryAccountNumber());
			trip.setStatus(tripGeneratorService.getTripStatus(tapOn.getStopId(), null));
		}
		return trip;
	}

	/**
	 * readTapsFromInputFile method reads list of taps from the input file.
	 * 
	 * @param inputFilePath
	 * @return list of tap's
	 * @throws InputFileException
	 */
	List<Tap> readTapsFromInputFile(String inputFilePath) throws InputFileException {
		List<Tap> taps = null;
		ObjectMapper mapper = getObjectMapper();
		Taps tapsWrapper;
		try {
			tapsWrapper = mapper.readValue(Paths.get(inputFilePath).toFile(), Taps.class);
			taps = tapsWrapper.getTaps();
		} catch (IOException e) {
			throw new InputFileException();
		}

		return taps;
	}

	/**
	 * writeTripsToOutputFile method writes list of trips into output file.
	 * 
	 * @param trips
	 * @param outputFilePath
	 * @throws OutputFileException
	 */
	void writeTripsToOutputFile(List<Trip> trips, String outputFilePath) throws OutputFileException {
		ObjectMapper mapper = getObjectMapper();
		// Create ObjectWriter using DefaultPrettyPrinter instance to format the output
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		Trips tripsWrapper = new Trips();
		tripsWrapper.setTrips(trips);
		try {
			writer.writeValue(Paths.get(outputFilePath).toFile(), tripsWrapper);
		} catch (IOException e) {
			throw new OutputFileException();
		}
	}

	private ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// Need to register JavaTimeModule to work with Java 8 ZonedDateTime
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}
}
