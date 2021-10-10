package net.codingchallenge.tripgenerator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import net.codingchallenge.tripgenerator.enums.TapType;
import net.codingchallenge.tripgenerator.enums.TripStatus;
import net.codingchallenge.tripgenerator.exception.TripGenerationException;
import net.codingchallenge.tripgenerator.model.Tap;

/**
 * The TripGeneratorServiceTest class performs unit tests of each service method
 * using a test data set.
 * 
 * @author Gihan Rajakaruna
 *
 */
@ActiveProfiles("test")
@SpringBootTest
public class TripGeneratorServiceTest {

	@Autowired
	private TripGeneratorService tripGeneratorService;

	List<Tap> taps = new ArrayList<Tap>();

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("UTC"));

	public TripGeneratorServiceTest() throws ParseException {
		// Initialize test data
		taps.add(new Tap(1, ZonedDateTime.parse("22-01-2021 13:00:00", formatter), TapType.ON, "Stop1", "Company1",
				"Bus37", "5500005555555559"));
		taps.add(new Tap(2, ZonedDateTime.parse("22-01-2021 13:05:00", formatter), TapType.OFF, "Stop2", "Company1",
				"Bus37", "5500005555555559"));
		taps.add(new Tap(3, ZonedDateTime.parse("22-01-2021 13:07:00", formatter), TapType.ON, "Stop3", "Company2",
				"Bus14", "6011000400000000"));
		taps.add(new Tap(4, ZonedDateTime.parse("22-01-2021 13:09:30", formatter), TapType.ON, "Stop2", "Company2",
				"Bus19", "4462030000000000"));
		taps.add(new Tap(5, ZonedDateTime.parse("22-01-2021 13:14:00", formatter), TapType.OFF, "Stop3", "Company2",
				"Bus19", "4462030000000000"));
	}

	/**
	 * Test getTapOns method
	 */
	@Test
	void getTapOnsTest() {
		// There are 3 ON taps in input data
		List<Tap> tapOnList = tripGeneratorService.getAllTapOns(taps);
		assertEquals(3, tapOnList.size());
	}

	/**
	 * Test getTapOff method
	 */
	@Test
	void getTapOffTest() {
		// Tap Id 2 is the corresponding OFF tap of Tap Id 1
		Tap tapOff = tripGeneratorService.getTapOff(taps, taps.get(0));
		assertEquals(2, tapOff.getId());
		// Tap Id 3 doesn't have a OFF tap entry
		tapOff = tripGeneratorService.getTapOff(taps, taps.get(2));
		assertNull(tapOff);
		// Tap Id 5 is the corresponding OFF tap of Tap Id 4
		tapOff = tripGeneratorService.getTapOff(taps, taps.get(3));
		assertEquals(5, tapOff.getId());
	}

	/**
	 * Test getTripDuration method
	 * 
	 * @throws TripGenerationException
	 */
	@Test
	void getTripDurationTest() throws TripGenerationException {
		// Duration between Tap Id's 1 and 2 is 300 seconds
		long duration = tripGeneratorService.getTripDuration(taps.get(0).getDatetimeUTC(),
				taps.get(1).getDatetimeUTC());
		assertEquals(300, duration);
		// Duration between Tap Id's 4 and 5 is 270 seconds
		duration = tripGeneratorService.getTripDuration(taps.get(3).getDatetimeUTC(), taps.get(4).getDatetimeUTC());
		assertEquals(270, duration);
	}

	/**
	 * Test getTripFare method for all possible combinations
	 * 
	 * @throws TripGenerationException
	 */
	@Test
	void getTripFareTest() throws TripGenerationException {
		// Max cost for trip from Stop1 is $7.30
		double tripFare = tripGeneratorService.getTripFare("Stop1");
		assertEquals(7.30, tripFare);
		// Max cost for trip from Stop2 is $5.50
		tripFare = tripGeneratorService.getTripFare("Stop2");
		assertEquals(5.50, tripFare);
		// Max cost for trip from Stop3 is $7.30
		tripFare = tripGeneratorService.getTripFare("Stop3");
		assertEquals(7.30, tripFare);
		// Cost for trip between Stop1 and Stop2 is $3.25
		tripFare = tripGeneratorService.getTripFare("Stop1", "Stop2");
		assertEquals(3.25, tripFare);
		// Cost for trip between Stop2 and Stop1 is $3.25
		tripFare = tripGeneratorService.getTripFare("Stop2", "Stop1");
		assertEquals(3.25, tripFare);
		// Cost for trip between Stop1 and Stop3 is $7.30
		tripFare = tripGeneratorService.getTripFare("Stop1", "Stop3");
		assertEquals(7.30, tripFare);
		// Cost for trip between Stop3 and Stop1 is $7.30
		tripFare = tripGeneratorService.getTripFare("Stop3", "Stop1");
		assertEquals(7.30, tripFare);
		// Cost for trip between Stop2 and Stop3 is $5.50
		tripFare = tripGeneratorService.getTripFare("Stop2", "Stop3");
		assertEquals(5.50, tripFare);
		// Cost for trip between Stop3 and Stop2 is $5.50
		tripFare = tripGeneratorService.getTripFare("Stop3", "Stop2");
		assertEquals(5.50, tripFare);
	}

	/**
	 * Test getTripStatus method for possible combinations
	 * 
	 * @throws TripGenerationException
	 */
	@Test
	void getTripStatusTest() throws TripGenerationException {
		// Trip status is INCOMPLETE when there is no OFF tap
		TripStatus tripStatus = tripGeneratorService.getTripStatus("Stop1", null);
		assertEquals(TripStatus.INCOMPLETE, tripStatus);
		// Trip status is INCOMPLETE when there is no OFF tap
		tripStatus = tripGeneratorService.getTripStatus("Stop2", null);
		assertEquals(TripStatus.INCOMPLETE, tripStatus);
		// Trip status is INCOMPLETE when there is no OFF tap
		tripStatus = tripGeneratorService.getTripStatus("Stop3", null);
		assertEquals(TripStatus.INCOMPLETE, tripStatus);
		// Trip status is CANCELLED if the source and destination stops are the same
		tripStatus = tripGeneratorService.getTripStatus("Stop1", "Stop1");
		assertEquals(TripStatus.CANCELLED, tripStatus);
		// Trip status is CANCELLED if the source and destination stops are the same
		tripStatus = tripGeneratorService.getTripStatus("Stop2", "Stop2");
		assertEquals(TripStatus.CANCELLED, tripStatus);
		// Trip status is CANCELLED if the source and destination stops are the same
		tripStatus = tripGeneratorService.getTripStatus("Stop3", "Stop3");
		assertEquals(TripStatus.CANCELLED, tripStatus);
		// Trip status is COMPLETED when ON and OFF taps are in different stops
		tripStatus = tripGeneratorService.getTripStatus("Stop1", "Stop2");
		assertEquals(TripStatus.COMPLETED, tripStatus);
		// Trip status is COMPLETED when ON and OFF taps are in different stops
		tripStatus = tripGeneratorService.getTripStatus("Stop1", "Stop3");
		assertEquals(TripStatus.COMPLETED, tripStatus);
		// Trip status is COMPLETED when ON and OFF taps are in different stops
		tripStatus = tripGeneratorService.getTripStatus("Stop2", "Stop1");
		assertEquals(TripStatus.COMPLETED, tripStatus);
		// Trip status is COMPLETED when ON and OFF taps are in different stops
		tripStatus = tripGeneratorService.getTripStatus("Stop2", "Stop3");
		assertEquals(TripStatus.COMPLETED, tripStatus);
		// Trip status is COMPLETED when ON and OFF taps are in different stops
		tripStatus = tripGeneratorService.getTripStatus("Stop3", "Stop1");
		assertEquals(TripStatus.COMPLETED, tripStatus);
		// Trip status is COMPLETED when ON and OFF taps are in different stops
		tripStatus = tripGeneratorService.getTripStatus("Stop3", "Stop2");
		assertEquals(TripStatus.COMPLETED, tripStatus);
	}
}
