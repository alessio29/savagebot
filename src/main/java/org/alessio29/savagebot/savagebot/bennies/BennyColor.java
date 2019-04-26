package org.alessio29.savagebot.savagebot.bennies;

public enum BennyColor {
	WHITE, RED, BLUE, GOLDEN;

	public static BennyColor getColor(String string) {
		
		if ("WHITE".equalsIgnoreCase(string)) {
			return WHITE;
		}
		if ("BLUE".equalsIgnoreCase(string)) {
			return BLUE;
		}
		if ("RED".equalsIgnoreCase(string)) {
			return RED;
		}
		if ("GOLDEN".equalsIgnoreCase(string)) {
			return GOLDEN;
		}
		return null;
		
	}		
}

