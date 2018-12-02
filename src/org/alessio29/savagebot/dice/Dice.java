package org.alessio29.savagebot.dice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.alessio29.savagebot.exceptions.WrongDieCodeException;
import org.alessio29.savagebot.internal.Messages;

public class Dice {

	public static DiceRollResult rollBasicDice(int count, int size, boolean explode) throws WrongDieCodeException {
		
		DiceRollResult finalResult = new DiceRollResult(count, size, 0, explode);
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
		
		DiceRollResult result = new DiceRollResult(1, size, 0, explode);
		long roll = roll(size);
		result.addResult(roll);
		result.addDetail(roll+"");
		while (explode && roll==size) {
			roll = roll(size);
			result.addDetail("+"+roll);
			result.addResult(roll);
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

		DiceRollResult result = new DiceRollResult(dieCount, dieSize, keepCount, false);
		for (int i=0; i<keepCount; i++) {
			result.combine(rolls.get(i), true);
		}
		for (int i=keepCount; i<dieCount; i++) {
			result.combine(rolls.get(i), false);
		}
		
		return result;
	}


	public static DiceRollResult rollSavageDice(int traitDieSize, Integer wildDieSize) throws WrongDieCodeException {
		
		DiceRollResult trait = rollDie(traitDieSize, true);
		DiceRollResult wild = rollDie(wildDieSize, true);
		
		return DiceRollResult.makeSWResult(trait, wild);
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
		DiceRollResult result = new DiceRollResult(dieCount, dieSize, keepCount, false);
		for (int i=0; i<keepCount; i++) {
			result.combine(rolls.get(i), true);
		}
		for (int i=keepCount; i<dieCount; i++) {
			result.combine(rolls.get(i), false);
		}
		
		return result;	}


	public static DiceRollResult rollLadyBlackbirdDice(Integer dieCount, Integer dieSize, Integer successTreshold) throws WrongDieCodeException {
		
		DiceRollResult result = new DiceRollResult();
		result.setDieCode(dieCount+"d"+dieSize+">="+successTreshold);
		for (int i=0; i<dieCount;i++) {
			DiceRollResult roll =  rollDie(dieSize,false);
			if (roll.getResult()>=successTreshold) {
				result.addDetail(Messages.bold(roll.getDetails()));
				result.setResult(result.getResult()+1);
			} else {
				result.addDetail(roll.getDetails());
			}
		}
		return result;
	}
}
