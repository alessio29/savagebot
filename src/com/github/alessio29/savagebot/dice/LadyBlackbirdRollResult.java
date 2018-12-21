package com.github.alessio29.savagebot.dice;

public class LadyBlackbirdRollResult extends DiceRollResult {

	public LadyBlackbirdRollResult(Integer dieCount, Integer dieSize, Integer successTreshold) {
		this.setDieCode(dieCount+"d"+dieSize+">="+successTreshold);
	}

}
