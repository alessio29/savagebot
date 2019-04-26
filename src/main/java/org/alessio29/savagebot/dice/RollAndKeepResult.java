package org.alessio29.savagebot.dice;

public class RollAndKeepResult extends DiceRollResult {

	public RollAndKeepResult(Integer dieCount, Integer dieSize, Integer keepCount) {

		this.setDieCode(dieCount+"d"+dieSize+"k"+keepCount);
	}
}
