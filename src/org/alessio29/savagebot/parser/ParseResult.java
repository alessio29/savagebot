package org.alessio29.savagebot.parser;

public class ParseResult {

	private int accumulator;
	private String remainder;

	public ParseResult(int v, String r)
	{
		this.setAccumulator(v);
		this.setRemainder(r);
	}

	public int getAccumulator() {
		return accumulator;
	}

	public void setAccumulator(int accumulator) {
		this.accumulator = accumulator;
	}

	public String getRemainder() {
		return remainder;
	}

	public void setRemainder(String remainder) {
		this.remainder = remainder;
	}
}
