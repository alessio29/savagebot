package com.github.alessio29.savagebot.dice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.github.alessio29.savagebot.exceptions.WrongDieCodeException;

public class Dice {

	public static DiceRollResult rollBasicDice(int count, int size, boolean explode) throws WrongDieCodeException {
		
		DiceRollResult finalResult = new BasicRollResult(count, size, explode);
		for (int i=0; i<count; i++) {
			DiceRollResult currentDie = rollDie(size, explode);
			finalResult.addDie(currentDie);
		}
		return finalResult;
	}

	
	private static DiceRollResult rollDie(int size, boolean explode) throws WrongDieCodeException {
		
		if (size<2) {
			throw new WrongDieCodeException();
		}
		
		DiceRollResult result = new BasicRollResult(1, size, explode);
		long roll = roll(size);
		result.setResult(roll);
		result.setDetails(roll+"");
		while (explode && roll==size) {
			roll = roll(size);
			result.setDetails(result.getDetails()+"+"+roll);
			result.setResult(result.getResult()+roll);
		}
		if (result.getDetails().contains("+")) {
			result.setDetails("["+result.getDetails()+"]");
		} 
		return result;
	}

	private static long roll(int size) {
		return (long)(Math.random()*size)+1;
	}


	public static DiceRollResult rollRollKeepDice(Integer dieCount, Integer dieSize, Integer keepCount) throws WrongDieCodeException {

		ArrayList<DiceRollResult> rolls = new ArrayList<DiceRollResult>();
		for (int i=0; i<dieCount;i++) {
			rolls.add(rollDie(dieSize,false));
		}
		Collections.sort(rolls, new Comparator<DiceRollResult>() {
			@Override
			public int compare(DiceRollResult o1, DiceRollResult o2) {
				return (int) (o2.getResult()-o1.getResult());
			}
			
		});

		DiceRollResult result = new RollAndKeepResult(dieCount, dieSize, keepCount);
		for (int i=0; i<keepCount; i++) {
			result.addMeaningfulValue(rolls.get(i));
		}
		for (int i=keepCount; i<dieCount; i++) {
			result.addIgnoredValue(rolls.get(i));
		}
		
		return result;
	}


	public static DiceRollResult rollSavageDice(int traitDieSize, Integer wildDieSize) throws WrongDieCodeException {
		
		DiceRollResult traitRoll = rollDie(traitDieSize, true);
		DiceRollResult wildRoll = rollDie(wildDieSize, true);
		
		return new SavageWorldRollResult(traitRoll, wildRoll);
	}


	public static DiceRollResult rollRollKeepLowestDice(Integer dieCount, Integer dieSize, Integer keepCount) throws WrongDieCodeException {
		
		ArrayList<DiceRollResult> rolls = new ArrayList<DiceRollResult>();
		for (int i=0; i<dieCount;i++) {
			rolls.add(rollDie(dieSize, false));
		}
		Collections.sort(rolls, new Comparator<DiceRollResult>() {
			@Override
			public int compare(DiceRollResult o1, DiceRollResult o2) {
				return (int) (o1.getResult()-o2.getResult());
			}
			
		});
		DiceRollResult result = new RollAndKeepResult(dieCount, dieSize, keepCount);
		for (int i=0; i<keepCount; i++) {
			result.addMeaningfulValue(rolls.get(i));
		}
		for (int i=keepCount; i<dieCount; i++) {
			result.addIgnoredValue(rolls.get(i));
		}
		
		return result;	}


	public static DiceRollResult rollLadyBlackbirdDice(Integer dieCount, Integer dieSize, Integer successTreshold) throws WrongDieCodeException {
		
		DiceRollResult result = new LadyBlackbirdRollResult(dieCount, dieSize, successTreshold);
		
		long successCount = 0l;
		for (int i=0; i<dieCount;i++) {
			DiceRollResult roll =  rollDie(dieSize,false);
			if (roll.getResult()>=successTreshold) {
				result.addMeaningfulValue(roll);
				successCount++;
			} else {
				result.addIgnoredValue(roll);
			}
		}
		
		result.setResult(successCount);
		return result;
	}


	public static DiceRollResult rollD66Dice() throws WrongDieCodeException {
		
		DiceRollResult roll1 =  rollDie(6,false);
		DiceRollResult roll2 =  rollDie(6,false);
		
		return new D66RollResult(roll1, roll2);
	}


	public static DiceRollResult rollFudgeDice(Integer dieCount) throws WrongDieCodeException {
		
		DiceRollResult result = new DiceRollResult();
		ArrayList<String> rep = new ArrayList<>(dieCount);
		for (int i=0; i<dieCount; i++ ) {
			DiceRollResult d = rollDie(3,false);
			long val = d.getResult()-2;
			String dieCode = (val==0)?"0":((val<0)?"-":"+");
			result.setResult(result.getResult()+val);
			rep.add(dieCode);
		}
		result.setDetails(String.join(";", rep));
		return result;
	}
}
