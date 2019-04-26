package org.alessio29.savagebot.dice;

import org.alessio29.savagebot.internal.Messages;

import java.util.ArrayList;

public class SavageWorldsMultiDiceResult extends DiceRollResult {

	public SavageWorldsMultiDiceResult(ArrayList<DiceRollResult> sorted) {
		
		int last = sorted.size()-1;
		int min = sorted.get(last-1).getIntResult();
		DiceRollResult wild = sorted.get(last);
		boolean wildBold = true;
		if (wild.getIntResult() < min) {
			wildBold = false;
			min = wild.getIntResult();
		}
		for (int i=0; i<last-1; i++) {
			this.setDieCode(this.getDieCode()+sorted.get(i).getDieCode()+"; ");
			String curDetails = sorted.get(i).getDetails();
			String curResult = sorted.get(i).getIntResult()+"";
			curDetails = Messages.bold(curDetails);
			curResult = Messages.bold(curResult);
			if (this.getResult().trim().isEmpty()) {
				this.setResult(curResult);	
			} else {
				this.setResult(this.getResult()+"; "+curResult);
			}
			if (this.getDetails().trim().isEmpty()) {
				this.setDetails(curDetails);	
			} else {
				this.setDetails(this.getDetails()+"; "+curDetails);
			}
		}
		String lastRes = sorted.get(last-1).getResult();
		String lastDet = sorted.get(last-1).getDetails();
		
		if (!wildBold) {
			lastRes = Messages.bold(lastRes);
			lastDet = Messages.bold(lastDet);
		}
		
		this.setDieCode(this.getDieCode()+sorted.get(last-1).getDieCode()+"; ");
		
		if (this.getResult().trim().isEmpty()) {
			this.setResult(lastRes);	
		} else {
			this.setResult(this.getResult()+"; "+lastRes);
		}
		if (this.getDetails().trim().isEmpty()) {
			this.setDetails(lastDet);	
		} else {
			this.setDetails(this.getDetails()+"; "+lastDet);
		}
		
		this.setDieCode(this.getDieCode()+sorted.get(last).getDieCode());
		this.setDieCode("max"+last+"("+this.getDieCode()+")");
		String wildDetails = (wildBold)?Messages.bold(wild.getDetails()):wild.getDetails();
		String wildResult = (wildBold)?Messages.bold(wild.getResult()):wild.getResult();
		this.setDetails(this.getDetails()+"; "+wildDetails);
		this.setResult(this.getResult()+"; "+wildResult);
	}

}
