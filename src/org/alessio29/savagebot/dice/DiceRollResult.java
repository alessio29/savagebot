package org.alessio29.savagebot.dice;

import org.alessio29.savagebot.exceptions.ParseErrorException;
import org.alessio29.savagebot.internal.Messages;

public class DiceRollResult {

	private String original="";
	private String dieCode="";
	private long result = 0l;
	private String details = "";
	private boolean explode = false;
	
	public DiceRollResult(Integer dieCount, Integer dieSize, Integer keepCount, boolean explode) {
		this.dieCode = createDieCode(dieCount, dieSize, keepCount, explode);
		this.explode=explode;
		this.original="";
	}

	
	
	public DiceRollResult() {
		this.original="";
	}

	public DiceRollResult(String unparseable) {
		this.original= unparseable;
	}

	public void addResult(long newResult) {
		this.result= this.result + newResult;
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
	
//	public void combine(DiceRollResult rollDie, boolean isKept) {
//		
//		this.details += (this.details.trim().isEmpty())?rollDie.getDetails():"+"+rollDie.getDetails(); 
//		if(isKept) {
//			this.result+=rollDie.getResult();	
//		}
//	}

	@Override
	public String toString() {
		if (this.original.trim().isEmpty()) {
			return dieCode+"=" + details + "="+result;	
		}
		return this.original;
	}

	public String getDieCode() {
		return dieCode;
	}

	public void setDieCode(String dieCode) {
		this.dieCode = dieCode;
	}

	public static DiceRollResult makeSWResult(DiceRollResult trait, DiceRollResult wild) {
		long res = 0;
		if (trait.getResult()>wild.getResult()) {
			trait.setDetails(Messages.bold(trait.getDetails()));
			res = trait.getResult();
		} else {
			wild.setDetails(Messages.bold(wild.getDetails()));
			res = wild.getResult();
		}
		DiceRollResult result = new DiceRollResult(1, 2, 0, true);
		result.setDieCode("max("+trait.getDieCode()+";"+wild.getDieCode()+")");
		
		result.setDetails("("+trait.getDetails()+";"+wild.getDetails()+")" );
		result.setResult(res);
		return result;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public boolean isString() {
		return !original.isEmpty();
	}

	private String createDieCode(Integer dieCount, Integer dieSize, Integer keepCount, boolean explode) {
		return ((dieCount>1)?dieCount:"")+"d"+ dieSize+((explode)?"!":"") + ((keepCount>0)?"k"+keepCount:"");
	}

	public void addDie(DiceRollResult currentDie) {
		this.result+=currentDie.result;
		this.details+=((this.details.isEmpty())?"":"+")+currentDie.details;
	}



	public boolean isExplode() {
		return explode;
	}



	public void setExplode(boolean explode) {
		this.explode = explode;
	}



	public void addMeaningfulValue(DiceRollResult diceRollResult) {
		addValue(diceRollResult, true);	
	}



	public void addIgnoredValue(DiceRollResult diceRollResult) {
		addValue(diceRollResult, false);		
	}
	
	private void addValue(DiceRollResult diceRollResult, boolean makeBold) {
		
		String value = diceRollResult.details;
		if (makeBold) {
			value = Messages.bold(value);
		}
		this.result +=diceRollResult.result;
		this.details = (this.details.isEmpty())?value:this.details+"+"+value;	
	}
	
}