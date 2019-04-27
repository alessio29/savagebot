package org.alessio29.savagebot.dice;

import org.alessio29.savagebot.exceptions.WrongDieCodeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

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
		int roll = roll(size);
		result.setResult(roll+"");
		result.setDetails(roll+"");
		while (explode && roll==size) {
			roll = roll(size);
			result.setDetails(result.getDetails()+"+"+roll);
			result.setResult(result.getIntResult()+roll+"");
		}
		if (result.getDetails().contains("+")) {
			result.setDetails("["+result.getDetails()+"]");
		} 
		return result;
	}

	public static final Random RANDOM = new Random();

	private static int roll(int size) {
		return RANDOM.nextInt(size) + 1;
	}


	public static DiceRollResult rollRollKeepDice(Integer dieCount, Integer dieSize, Integer keepCount) throws WrongDieCodeException {

		ArrayList<DiceRollResult> rolls = new ArrayList<DiceRollResult>();
		for (int i=0; i<dieCount;i++) {
			rolls.add(rollDie(dieSize,false));
		}
		Collections.sort(rolls, new Comparator<DiceRollResult>() {
			@Override
			public int compare(DiceRollResult o1, DiceRollResult o2) {
				return (o2.getIntResult()-o1.getIntResult());
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


	public static DiceRollResult rollSavageDice(int traitDiceCount, int traitDieSize, Integer wildDieSize) throws WrongDieCodeException {
		
		if (traitDiceCount == 1) {
			DiceRollResult trait = rollDie(traitDieSize, true);
			DiceRollResult wild = rollDie(wildDieSize, true);
			return new SavageWorldRollResult(trait, wild);
		}
		
		ArrayList<DiceRollResult> sorted = new ArrayList<>();
		for (int i=0; i<traitDiceCount; i++) {
			sorted.add(rollDie(traitDieSize, true));
		}
		Collections.sort(sorted, new Comparator<DiceRollResult>() {
			@Override
			public int compare(DiceRollResult o1, DiceRollResult o2) {
				return (o2.getIntResult()-o1.getIntResult());
			}
			
		});
		sorted.add(rollDie(wildDieSize, true)); // wild die is last
		return new SavageWorldsMultiDiceResult(sorted);
	}


	public static DiceRollResult rollRollKeepLowestDice(Integer dieCount, Integer dieSize, Integer keepCount) throws WrongDieCodeException {
		
		ArrayList<DiceRollResult> rolls = new ArrayList<DiceRollResult>();
		for (int i=0; i<dieCount;i++) {
			rolls.add(rollDie(dieSize, false));
		}
		Collections.sort(rolls, new Comparator<DiceRollResult>() {
			@Override
			public int compare(DiceRollResult o1, DiceRollResult o2) {
				return (o1.getIntResult()-o2.getIntResult());
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


	public static DiceRollResult rollLadyBlackbirdDice(Integer dieCount, Integer dieSize, Integer successTreshold) throws WrongDieCodeException {
		
		DiceRollResult result = new LadyBlackbirdRollResult(dieCount, dieSize, successTreshold);
		
		int successCount = 0;
		for (int i=0; i<dieCount;i++) {
			DiceRollResult roll =  rollDie(dieSize,false);
			if (roll.getIntResult()>=successTreshold) {
				result.addMeaningfulValue(roll);
				successCount++;
			} else {
				result.addIgnoredValue(roll);
			}
		}
		result.setResult(successCount+"");
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
			long val = d.getIntResult()-2;
			String dieCode = (val==0)?"0":((val<0)?"-":"+");
			result.setResult(result.getIntResult()+val+"");
			rep.add(dieCode);
		}
		result.setDetails(String.join(";", rep));
		return result;
	}
}
