package org.alessio29.savagebot.dice;

import org.alessio29.savagebot.internal.Messages;

public class SavageWorldRollResult extends DiceRollResult {
	
	public SavageWorldRollResult(DiceRollResult trait, DiceRollResult wild) {
		
		long res = 0;
		if (trait.getIntResult()>wild.getIntResult()) {
			trait.setDetails(Messages.bold(trait.getDetails()));
			res = trait.getIntResult();
		} else {
			wild.setDetails(Messages.bold(wild.getDetails()));
			res = wild.getIntResult();
		}
		this.setDieCode("max("+trait.getDieCode()+";"+wild.getDieCode()+")");
		
		this.setDetails("("+trait.getDetails()+";"+wild.getDetails()+")" );
		this.setResult(res+"");
	}
	
}
