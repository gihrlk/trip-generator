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

	/**
	 * Input file contains a single journey. It will generate a COMPLETED trip.
	 * Method validates each attribute in tap and also verifies the COMPLETED trip's
	 * attributes.
	 * 
	 * @throws InputFileException
	 * @throws TripGenerationException
	 * @throws OutputFileException
	 * @throws ParseException
	 */
	@Test
	void readTapsFromInputFileTest1()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-1.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-1.json");

		// There are 2 taps in input file
		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		assertEquals(2, taps.size());

		// Verify each attribute in ON tap
		Tap tap = taps.get(0);
		assertEquals(1, tap.getId());
		assertEquals(ZonedDateTime.parse("22-01-2021 13:00:00", formatter), tap.getDatetimeUTC());
		assertEquals(TapType.ON, tap.getTapType());
		assertEquals("Stop1", tap.getStopId());
		assertEquals("Company1", tap.getCompanyId());
		assertEquals("Bus37", tap.getBusId());
		assertEquals("5500005555555559", tap.getPrimaryAccountNumber());

		// Verify each attribute in OFF tap
		tap = taps.get(1);
		assertEquals(2, tap.getId());
		assertEquals(ZonedDateTime.parse("22-01-2021 13:05:00", formatter), tap.getDatetimeUTC());
		assertEquals(TapType.OFF, tap.getTapType());
		assertEquals("Stop2", tap.getStopId());
		assertEquals("Company1", tap.getCompanyId());
		assertEquals("Bus37", tap.getBusId());
		assertEquals("5500005555555559", tap.getPrimaryAccountNumber());

		// A COMPLETED trip should be generated
		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		assertEquals(1, trips.size());

		// Check each attribute in COMPLETED trip
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

		// Make sure the output file is saved
		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		assertTrue(outputFile.exists());
	}

	/**
	 * Input file contains a single journey. But the CompanyId and BusId are
	 * different. Therefore, an INCOMPLETE trip will be generated. Method verifies
	 * INCOMPLETE trip's attributes.
	 * 
	 * @throws InputFileException
	 * @throws TripGenerationException
	 * @throws OutputFileException
	 * @throws ParseException
	 */
	@Test
	void readTapsFromInputFileTest2()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-2.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-2.json");

		// There are 2 taps in input file
		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		assertEquals(2, taps.size());

		// An INCOMPLETE trip should be generated
		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		assertEquals(1, trips.size());

		// Check each attribute in INCOMPLETE trip
		Trip trip = trips.get(0);
		assertEquals(ZonedDateTime.parse("22-01-2021 13:00:00", formatter), trip.getStarted());
		assertEquals(null, trip.getFinished());
		assertEquals(null, trip.getDurationSecs());
		assertEquals("Stop1", trip.getFromStopId());
		assertEquals(null, trip.getToStopId());
		assertEquals(7.30, trip.getChargeAmount());
		assertEquals("Company1", trip.getCompanyId());
		assertEquals("Bus37", trip.getBusId());
		assertEquals("5500005555555559", trip.getPrimaryAccountNumber());
		assertEquals(TripStatus.INCOMPLETE, trip.getStatus());

		// Make sure the output file is saved
		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		assertTrue(outputFile.exists());
	}

	/**
	 * Input file contains multiple journeys of the same customer. This will
	 * generate two COMPLETED trips. Method verifies the status of all generated
	 * trips.
	 * 
	 * @throws InputFileException
	 * @throws TripGenerationException
	 * @throws OutputFileException
	 * @throws ParseException
	 */
	@Test
	void readTapsFromInputFileTest3()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-3.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-3.json");

		// There are 4 taps in input file
		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		assertEquals(4, taps.size());

		// 2 COMPLETED trips should be generated
		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		assertEquals(2, trips.size());

		// Verify the status of each trip
		Trip trip = trips.get(0);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(1);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());

		// Make sure the output file is saved
		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		assertTrue(outputFile.exists());
	}

	/**
	 * Input file contains multiple journeys of the same customer. This will
	 * generate two COMPLETED trips and one CANCELLED trip. Method verifies the
	 * status of all generated trips.
	 * 
	 * @throws InputFileException
	 * @throws TripGenerationException
	 * @throws OutputFileException
	 * @throws ParseException
	 */
	@Test
	void readTapsFromInputFileTest4()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-4.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-4.json");

		// There are 6 taps in input file
		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		assertEquals(6, taps.size());

		// 2 COMPLETED trips and one CANCELLED trip should be generated
		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		assertEquals(3, trips.size());

		// Verify the status of each trip
		Trip trip = trips.get(0);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(1);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(2);
		assertEquals(TripStatus.CANCELLED, trip.getStatus());

		// Check each attribute in CANCELLED trip
		assertEquals(ZonedDateTime.parse("22-01-2021 13:18:00", formatter), trip.getStarted());
		assertEquals(ZonedDateTime.parse("22-01-2021 13:18:30", formatter), trip.getFinished());
		assertEquals(30, trip.getDurationSecs());
		assertEquals("Stop3", trip.getFromStopId());
		assertEquals("Stop3", trip.getToStopId());
		assertEquals(0.00, trip.getChargeAmount());
		assertEquals("Company1", trip.getCompanyId());
		assertEquals("Bus37", trip.getBusId());
		assertEquals("5500005555555559", trip.getPrimaryAccountNumber());
		assertEquals(TripStatus.CANCELLED, trip.getStatus());

		// Make sure the output file is saved
		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		assertTrue(outputFile.exists());
	}

	/**
	 * Input file contains multiple journeys of the same customer. This will
	 * generate two COMPLETED trips, one CANCELLED trip and one INCOMPLETE trip.
	 * Method verifies the status of all generated trips.
	 * 
	 * @throws InputFileException
	 * @throws TripGenerationException
	 * @throws OutputFileException
	 * @throws ParseException
	 */
	@Test
	void readTapsFromInputFileTest5()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-5.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-5.json");

		// There are 7 taps in input file
		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		assertEquals(7, taps.size());

		// 2 COMPLETED trips, 1 CANCELLED trip and 1 INCOMPLETE trip should be generated
		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		assertEquals(4, trips.size());

		// Verify the status of each trip
		Trip trip = trips.get(0);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(1);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(2);
		assertEquals(TripStatus.CANCELLED, trip.getStatus());
		trip = trips.get(3);
		assertEquals(TripStatus.INCOMPLETE, trip.getStatus());

		// Make sure the output file is saved
		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		assertTrue(outputFile.exists());
	}

	/**
	 * Input file contains multiple journeys of multiple customers. This will
	 * generate five COMPLETED trips, two CANCELLED trips and two INCOMPLETE trips.
	 * Method verifies the status of all generated trips.
	 * 
	 * @throws InputFileException
	 * @throws TripGenerationException
	 * @throws OutputFileException
	 * @throws ParseException
	 */
	@Test
	void readTapsFromInputFileTest6()
			throws InputFileException, TripGenerationException, OutputFileException, ParseException {

		File inputFile = new File("src/main/resources/tests/input/tap-data-6.json");
		File outputFile = new File("src/main/resources/tests/output/trip-data-6.json");

		// There are 16 taps in input file
		List<Tap> taps = tripGeneratorController.readTapsFromInputFile(inputFile.getAbsolutePath());
		assertEquals(16, taps.size());

		// 5 COMPLETED trips, 2 CANCELLED trips and 2 INCOMPLETE trips should be
		// generated
		List<Trip> trips = tripGeneratorController.generateTripsFromTaps(taps);
		assertEquals(9, trips.size());

		// Verify the status of each trip
		Trip trip = trips.get(0);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(1);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(2);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(3);
		assertEquals(TripStatus.CANCELLED, trip.getStatus());
		trip = trips.get(4);
		assertEquals(TripStatus.INCOMPLETE, trip.getStatus());
		trip = trips.get(5);
		assertEquals(TripStatus.CANCELLED, trip.getStatus());
		trip = trips.get(6);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(7);
		assertEquals(TripStatus.COMPLETED, trip.getStatus());
		trip = trips.get(8);
		assertEquals(TripStatus.INCOMPLETE, trip.getStatus());

		// Make sure the output file is saved
		tripGeneratorController.writeTripsToOutputFile(trips, outputFile.getAbsolutePath());
		assertTrue(outputFile.exists());
	}
}
