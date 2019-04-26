package com.github.alessio29.savagebot.dice;

public class D66RollResult extends DiceRollResult{

	public D66RollResult(DiceRollResult roll1, DiceRollResult roll2) {
		
		this.setExplode(false);
		this.setResult(roll1.getIntResult()*10l+roll2.getIntResult()+"");
		this.setDieCode("d66");
		this.setDetails(roll1.getIntResult()+";"+roll2.getIntResult());
	}
}
