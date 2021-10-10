package net.codingchallenge.tripgenerator.enums;

public enum TapType {

	ON, OFF;

	public static TapType get(String value) {
		try {
			if (value != null) {
				return TapType.valueOf(value.toUpperCase());
			}
		} catch (IllegalArgumentException ex) {
			// not a valid TapType value
		}
		return null;
	}
}
