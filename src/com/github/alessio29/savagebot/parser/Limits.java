package com.github.alessio29.savagebot.parser;

public class Limits {

	public static final Limits NOLIMITS = new Limits();
	
	private int lower;
	private int upper;
	
	public Limits(int lowerLimit, int upperLimit) {
		this.lower = lowerLimit;
		this.upper = upperLimit;
	}

	public Limits() {
		this.lower = Integer.MIN_VALUE;
		this.upper = Integer.MAX_VALUE;
	}

}
