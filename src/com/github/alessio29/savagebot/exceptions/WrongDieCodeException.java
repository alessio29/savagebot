package com.github.alessio29.savagebot.exceptions;

public class WrongDieCodeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6317940364033194027L;

	public WrongDieCodeException () {
		super("Die size must be more than 2!");
	}
}
