package org.alessio29.savagebot.dice;

import org.alessio29.savagebot.exceptions.ParseErrorException;
import org.alessio29.savagebot.internal.Messages;

public class DiceRollResult {
	
	private String original="";
	private String dieCode="";
	private String result = "";
	private String details = "";
	private boolean explode = false;
	
	public DiceRollResult() {
	}
	
	public DiceRollResult(String unparseable) {
		this.original= unparseable;
	}

	public void addDie(DiceRollResult currentDie) {
		this.result =this.getIntResult()+currentDie.getIntResult()+"";
		this.details+=((this.details.isEmpty())?"":"+")+currentDie.details;
	}

	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	public int getIntResult() {
		if (result.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(result);
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getDieCode() {
		return dieCode;
	}

	public void setDieCode(String dieCode) {
		this.dieCode = dieCode;
	}
	
	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}
	
	public boolean isExplode() {
		return explode;
	}

	public void setExplode(boolean explode) {
		this.explode = explode;
	}
	
	@Override
	public String toString() {
		if (this.original.trim().isEmpty()) {
			return dieCode+"=" + details + "="+result;	
		}
		return this.original;
	}

	public void addMeaningfulValue(DiceRollResult diceRollResult) {
		this.result =this.getIntResult()+diceRollResult.getIntResult()+"";
		this.details = (this.details.isEmpty())? Messages.bold(diceRollResult.details):
			this.details+"+"+Messages.bold(diceRollResult.details);	
	}

	public void addIgnoredValue(DiceRollResult diceRollResult) {
		this.details = (this.details.isEmpty())?diceRollResult.details:
			this.details+"+"+diceRollResult.details;	
	}
	
	public boolean isString() {
		return !original.isEmpty();
	}

	public void add(DiceRollResult rollDie, String sign) throws ParseErrorException {
		
		if (sign.equals("+") || sign.equals("-")  ) {
			int val1 = Integer.parseInt(this.result);
			int val2 = Integer.parseInt(rollDie.result);

			if (sign.equals("+")) {
				this.result = val1+val2+"";
			} else {
				this.result = val1-val2+"";
			}
			this.dieCode += sign+rollDie.dieCode;
			if (this.details.isEmpty()) {
				this.details = rollDie.details;
				this.result = rollDie.result;
			} else {
				this.details = this.details+sign+rollDie.details;
			}
			return;
		}	
		throw new ParseErrorException("Wrong sign: + or - expected, but "+sign+" ecountered!"); 
	}

}