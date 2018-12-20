package com.github.alessio29.savagebot.dice;

public class DicelessRollResult extends DiceRollResult {

	public DicelessRollResult(String roll) {
		try {
			this.setResult(Integer.parseInt(roll));	
			this.setDetails(roll);
		} catch (Exception e) {
			this.setOriginal(roll);
		}
		this.setDieCode(roll);
		this.setDetails(roll);
	}

}
