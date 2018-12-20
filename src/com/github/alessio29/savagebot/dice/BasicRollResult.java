package com.github.alessio29.savagebot.dice;

public class BasicRollResult extends DiceRollResult {

	public BasicRollResult(int count, int size, boolean explode) {
		this.setDieCode(count+"d"+size+((explode)?"!":""));
		this.setExplode(explode);

	}

}
