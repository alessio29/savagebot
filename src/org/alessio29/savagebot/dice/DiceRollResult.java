package org.alessio29.savagebot.dice;

import org.alessio29.savagebot.exceptions.ParseErrorException;
import org.alessio29.savagebot.internal.Messages;

public class DiceRollResult {
	
	private String original="";
	private String dieCode="";
	private long result = 0l;
	private String details = "";
	private boolean explode = false;
	
	public DiceRollResult() {
	}
	
	public DiceRollResult(String unparseable) {
		this.original= unparseable;
	}

	public void addDie(DiceRollResult currentDie) {
		this.result+=currentDie.result;
		this.details+=((this.details.isEmpty())?"":"+")+currentDie.details;
	}

	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	public long getResult() {
		return result;
	}
	
	public void setResult(long result) {
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
		this.result +=diceRollResult.result;
		this.details = (this.details.isEmpty())?Messages.bold(diceRollResult.details):
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
			if (sign.equals("+")) {
				this.result += rollDie.result;
			} else {
				this.result -= rollDie.result;
			}
			this.dieCode += sign+rollDie.dieCode;
			if (this.details.isEmpty()) {
				this.details = rollDie.details;
				this.result = rollDie.result;
				return;
			} else {
				this.details = this.details+sign+rollDie.details;
				return;
			}
		}	
		throw new ParseErrorException("Wrong sign: + or - expected, but "+sign+" ecountered!"); 
	}
}