package org.alessio29.savagebot.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alessio29.savagebot.dice.Dice;
import org.alessio29.savagebot.dice.DiceRollResult;
import org.alessio29.savagebot.dice.DicelessRollResult;
import org.alessio29.savagebot.exceptions.ParseErrorException;
import org.alessio29.savagebot.exceptions.WrongDieCodeException;
import org.alessio29.savagebot.internal.Messages;

public class SimpleParser {

	private static final Pattern repeatRollPattern = Pattern.compile("^[0-9]+x");
	private static final Pattern basicAndExplodingPattern = Pattern.compile("^[0-9]*d[0-9]+!?$");
	private static final Pattern rollAndKeepPattern = Pattern.compile("^[0-9]*d[0-9]+k[0-9]+$"); 
	private static final Pattern rollAndKeepLowestPattern = Pattern.compile("^[0-9]*d[0-9]+kl[0-9]+$");
	private static final Pattern savageWorldsPattern = Pattern.compile("^(4|6|8|10|12)?s(4|6|8|10|12)+$");
	private static final Pattern ladyBlackbirdPattern = Pattern.compile("^[0-9]*d[0-9]+s[0-9]+$");
	
	private static final Pattern summPattern = Pattern.compile("^.+(\\+|-){1}.+$");
	private static final String[] PLUS_MINUS = {"+", "-"};
	
	public static String parseString(String s) throws ParseErrorException, WrongDieCodeException {
		String result = "";
		String[] rolls = s.split(" ");
		for (String roll: rolls) {
			List<DiceRollResult> rollResults = parseRepeatingDiceCode(roll);
			result = (result.isEmpty())?Messages.createStringRepresentation(rollResults):result+" "+Messages.createStringRepresentation(rollResults);
		}
		return result.trim();
	}

	private static List<DiceRollResult> parseRepeatingDiceCode(String roll) throws ParseErrorException, WrongDieCodeException {
		
		List<DiceRollResult> result = new ArrayList<DiceRollResult>();
		Matcher repeatMatcher = repeatRollPattern.matcher(roll);
		int repeatCount=1;
		String roll2repeat = roll;
		if(repeatMatcher.find()) {
			repeatCount=Integer.parseInt(roll.split("x")[0]);
			roll2repeat = roll.split("x")[1];
		} 
		for (int i=0; i<repeatCount; i++) {
			DiceRollResult rollDie = parseSumm(roll2repeat); 
			result.add(rollDie);
		}
		return result;
		
	}
	
	private static DiceRollResult parseSumm(String roll) throws ParseErrorException, WrongDieCodeException {
	
		Matcher summMatcher = summPattern.matcher(roll);
		if (summMatcher.find()) {
			String left = getLeftPart(roll, PLUS_MINUS);
			String right = getRightPart(roll, PLUS_MINUS);
			String sign = right.substring(0, 1);
			right = right.substring(1); // remove sign
			DiceRollResult leftRoll = new DiceRollResult(roll);
			if (!left.isEmpty()) {
				leftRoll = parseDiceCode(left);
			}
			if (leftRoll.isString()) {
				return new DiceRollResult(roll);
			}
			leftRoll.add(parseSumm(right), sign);
			return leftRoll;
		}
		return parseDiceCode(roll);
	}
	
	private static String getRightPart(String roll, String[] operators) {
	
		int first=getFirstOccurence(roll, operators);
		if (first>0) {
			return roll.substring(first);
		}
		return "";
	}

	private static String getLeftPart(String roll, String[] operators) {
		
		int first=getFirstOccurence(roll, operators);
		if (first>0) {
			return roll.substring(0, first);
		}
		return "";
	}
	
	private static int getFirstOccurence (String s1, String[] s2) {
		
		int first = Integer.MAX_VALUE; 
		for (String s: s2) {
			int current = s1.indexOf(s);
			if (current>0) {
				first = (first<current)?first:current;
			}
		}
		return first;
	}

	private static DiceRollResult parseDiceCode(String roll) throws ParseErrorException, WrongDieCodeException {

		Matcher basicExplodingMatcher = basicAndExplodingPattern.matcher(roll);
		if (basicExplodingMatcher.find()) {
			boolean explode = roll.endsWith("!");
			String[] s = roll.split("d");
			Integer dieCount = 1;
			if (!s[0].trim().isEmpty() ) {
				dieCount = Integer.parseInt(s[0]);	
			}
			String size = s[1];
			if (explode) {
				size = s[1].substring(0, s[1].length()-1);	
			}
			Integer dieSize=Integer.parseInt(size);
			return Dice.rollBasicDice(dieCount, dieSize, explode);
		}

		Matcher rollAndKeepLowestMatcher = rollAndKeepLowestPattern.matcher(roll);
		if(rollAndKeepLowestMatcher.find()) {
			String[] s = roll.split("d");
			Integer dieCount = 1;
			if (!s[0].trim().isEmpty() ) {
				dieCount = Integer.parseInt(s[0]);	
			}
			String[] d = s[1].split("kl");
			Integer dieSize=Integer.parseInt(d[0]);
			Integer keepCount = Integer.parseInt(d[1]);
			if (keepCount>dieCount) {
				throw new ParseErrorException("Keep dice count must be no more than total dice count!");				
			}
			return Dice.rollRollKeepLowestDice(dieCount, dieSize, keepCount);
		}
		
		Matcher rollAndKeepMatcher = rollAndKeepPattern.matcher(roll);
		if(rollAndKeepMatcher.find()) {
			String[] s = roll.split("d");
			Integer dieCount = 1;
			if (!s[0].trim().isEmpty() ) {
				dieCount = Integer.parseInt(s[0]);	
			}
			String[] d = s[1].split("k");
			Integer dieSize=Integer.parseInt(d[0]);
			Integer keepCount = Integer.parseInt(d[1]);
			if (keepCount>dieCount) {
				throw new ParseErrorException("Keep dice count must be no more than total dice count!");				
			}
			return Dice.rollRollKeepDice(dieCount, dieSize, keepCount);
		}
				
		Matcher savageWorldsMatcher = savageWorldsPattern.matcher(roll);
		if(savageWorldsMatcher.find()) {
			String[] s = roll.split("s");
			Integer wildDieSize = 6;
			if (!s[0].trim().isEmpty() ) {
				wildDieSize = Integer.parseInt(s[0]);	
			}
			int traitDieSize = Integer.parseInt(s[1]);
			return Dice.rollSavageDice(traitDieSize, wildDieSize);
		}
		
		Matcher ladyBlackbirdMatcher = ladyBlackbirdPattern.matcher(roll);
		if(ladyBlackbirdMatcher.find()) {
			String[] s = roll.split("d");
			Integer dieCount = 1;
			if (!s[0].trim().isEmpty() ) {
				dieCount = Integer.parseInt(s[0]);	
			}
			String[] d = s[1].split("s");
			Integer dieSize=Integer.parseInt(d[0]);
			Integer successTreshold = Integer.parseInt(d[1]);
			if (successTreshold>dieSize || successTreshold<2) {
				throw new ParseErrorException("Success threshold must be more than 1 and less than die size!");				
			}
			return Dice.rollLadyBlackbirdDice(dieCount, dieSize, successTreshold);
		}
		
		return new DicelessRollResult(roll);
	}
	
}
