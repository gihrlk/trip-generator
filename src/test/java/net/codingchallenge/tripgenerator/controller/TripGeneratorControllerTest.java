package net.codingchallenge.tripgenerator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import net.codingchallenge.tripgenerator.enums.TapType;
import net.codingchallenge.tripgenerator.enums.TripStatus;
import net.codingchallenge.tripgenerator.exception.InputFileException;
import net.codingchallenge.tripgenerator.exception.OutputFileException;
import net.codingchallenge.tripgenerator.exception.TripGenerationException;
import net.codingchallenge.tripgenerator.model.Tap;
import net.codingchallenge.tripgenerator.model.Trip;

/**
 * The TripGeneratorControllerTest class performs integrated testing by
 * executing all required methods to read input data, generate trips and write
 * the output file. The test json files used by this class is located at
 * src/main/resources/input directory.
 * 
 * @author Gihan Rajakaruna
 *
 */
@ActiveProfiles("test")
@SpringBootTest
public class TripGeneratorControllerTest {

	@Autowired
	private TripGeneratorController tripGeneratorController;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("UTC"));

	@Test
	void readTapsFromInputFileTest1()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-1.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-1.json");

		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		// There are 2 taps in input file
		assertEquals(2, taps.size());

		// Check each attribute in first tap
		Tap tap = taps.get(0);
		assertEquals(1, tap.getId());
		assertEquals(ZonedDateTime.parse("22-01-2021 13:00:00", formatter), tap.getDatetimeUTC());
		assertEquals(TapType.ON, tap.getTapType());
		assertEquals("Stop1", tap.getStopId());
		assertEquals("Company1", tap.getCompanyId());
		assertEquals("Bus37", tap.getBusId());
		assertEquals("5500005555555559", tap.getPrimaryAccountNumber());

		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		// One trip should be generated
		assertEquals(1, trips.size());

		// Check each attribute in first trip
		Trip trip = trips.get(0);
		assertEquals(ZonedDateTime.parse("22-01-2021 13:00:00", formatter), trip.getStarted());
		assertEquals(ZonedDateTime.parse("22-01-2021 13:05:00", formatter), trip.getFinished());
		assertEquals(300, trip.getDurationSecs());
		assertEquals("Stop1", trip.getFromStopId());
		assertEquals("Stop2", trip.getToStopId());
		assertEquals(3.25, trip.getChargeAmount());
		assertEquals("Company1", trip.getCompanyId());
		assertEquals("Bus37", trip.getBusId());
		assertEquals("5500005555555559", trip.getPrimaryAccountNumber());
		assertEquals(TripStatus.COMPLETED, trip.getStatus());

		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		// Make sure the file created
		assertTrue(outputFile.exists());
	}

	@Test
	void readTapsFromInputFileTest2()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-2.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-2.json");

		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		// There are 4 taps in input file
		assertEquals(4, taps.size());

		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		// 2 trips should be generated
		assertEquals(2, trips.size());

		// Check each attribute in first trip
		Trip trip = trips.get(0);
		assertEquals(ZonedDateTime.parse("22-01-2021 13:00:00", formatter), trip.getStarted());
		assertEquals(ZonedDateTime.parse("22-01-2021 13:05:00", formatter), trip.getFinished());
		assertEquals(300, trip.getDurationSecs());
		assertEquals("Stop1", trip.getFromStopId());
		assertEquals("Stop2", trip.getToStopId());
		assertEquals(3.25, trip.getChargeAmount());
		assertEquals("Company1", trip.getCompanyId());
		assertEquals("Bus37", trip.getBusId());
		assertEquals("5500005555555559", trip.getPrimaryAccountNumber());
		assertEquals(TripStatus.COMPLETED, trip.getStatus());

		// Check each attribute in second trip
		trip = trips.get(1);
		assertEquals(ZonedDateTime.parse("22-01-2021 13:09:30", formatter), trip.getStarted());
		assertEquals(ZonedDateTime.parse("22-01-2021 13:14:00", formatter), trip.getFinished());
		assertEquals(270, trip.getDurationSecs());
		assertEquals("Stop2", trip.getFromStopId());
		assertEquals("Stop3", trip.getToStopId());
		assertEquals(5.50, trip.getChargeAmount());
		assertEquals("Company1", trip.getCompanyId());
		assertEquals("Bus37", trip.getBusId());
		assertEquals("5500005555555559", trip.getPrimaryAccountNumber());
		assertEquals(TripStatus.COMPLETED, trip.getStatus());

		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		// Make sure the file created
		assertTrue(outputFile.exists());
	}

	@Test
	void readTapsFromInputFileTest3()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-3.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-3.json");

		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		// There are 6 taps in input file
		assertEquals(6, taps.size());

		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		// 3 trips should be generated
		assertEquals(3, trips.size());

		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		// Make sure the file created
		assertTrue(outputFile.exists());
	}

	@Test
	void readTapsFromInputFileTest4()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-4.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-4.json");

		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		// There are 7 taps in input file
		assertEquals(7, taps.size());

		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		// 4 trips should be generated
		assertEquals(4, trips.size());

		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		// Make sure the file created
		assertTrue(outputFile.exists());
	}

	@Test
	void readTapsFromInputFileTest5()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-5.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-5.json");

		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		// There are 16 taps in input file
		assertEquals(16, taps.size());

		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		// 9 trips should be generated
		assertEquals(9, trips.size());

		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		// Make sure the file created
		assertTrue(outputFile.exists());
	}
}
