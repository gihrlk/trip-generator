package net.codingchallenge.tripgenerator.enums;

public enum TripStatus {

	CANCELLED, COMPLETED, INCOMPLETE;

	public static TripStatus get(String value) {
		try {
			if (value != null) {
				return TripStatus.valueOf(value.toUpperCase());
			}
		} catch (IllegalArgumentException ex) {
			// not a valid TripStatus value
		}
		return null;
	}
}
