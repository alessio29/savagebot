package com.github.alessio29.savagebot.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.alessio29.savagebot.dice.Dice;
import com.github.alessio29.savagebot.dice.DiceRollResult;
import com.github.alessio29.savagebot.dice.DicelessRollResult;
import com.github.alessio29.savagebot.exceptions.ParseErrorException;
import com.github.alessio29.savagebot.exceptions.WrongDieCodeException;
import com.github.alessio29.savagebot.internal.Messages;

public class SimpleParser {

	private static final Pattern d66RollPattern = Pattern.compile("^[dDÍ ‰ƒ]66$");
	private static final Pattern repeatRollPattern = Pattern.compile("^[0-9]+x");
	private static final Pattern limitDamagePattern = Pattern.compile("^\\[\\S+\\]$");
	private static final Pattern basicAndExplodingPattern = Pattern.compile("^[0-9]*[dDÍ ‰ƒ][0-9]+!?$");
	private static final Pattern fudgePattern = Pattern.compile("^[0-9]*[dDÍ ‰ƒ][Ff‘Ù]?$");
	private static final Pattern rollAndKeepPattern = Pattern.compile("^[0-9]*[dDÍ ‰ƒ][0-9]+[kK·¡][0-9]+$"); 
	private static final Pattern rollAndKeepLowestPattern = Pattern.compile("^[0-9]*[dDÍ ‰ƒ][0-9]+(kl|KL|Ï|Ã)[0-9]+$");
	private static final Pattern savageWorldsPattern = Pattern.compile("^[0-9]*[sSÒ—](4|6|8|10|12)+$");
	private static final Pattern savageWorldsPatternWithCustomWildDiePattern = Pattern.compile("^[0-9]*[sSÒ—](4|6|8|10|12)[wW‰ƒ](4|6|8|10|12)+$");
	private static final Pattern ladyBlackbirdPattern = Pattern.compile("^[0-9]*[dDÍ ‰ƒ][0-9]+[sSÛ”][0-9]+$");
	
	private static final Pattern summPattern = Pattern.compile("^.+(\\+|-){1}.+$");
	private static final String[] PLUS_MINUS = {"+", "-"};
	
	public static String parseString(String s) throws ParseErrorException, WrongDieCodeException {
		String result = "";
		String[] rolls = s.split("\\s+");

		for (String roll: rolls) {
			List<DiceRollResult> rollResults = parseRepeatingDiceCode(roll);
			result = (result.isEmpty())?Messages.createStringRepresentation(rollResults) : result+Messages.createStringRepresentation(rollResults);
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
			Limits limits = new Limits();
			if (isLimitedRoll(roll2repeat)) {
				roll2repeat = roll2repeat.substring(1, roll2repeat.length()-1);
				limits = parseLimits(roll2repeat);	
			}
			DiceRollResult rollDie = parseSumm(roll2repeat, limits); 
			result.add(rollDie);
		}
		return result;
		
	}
	
	private static boolean isLimitedRoll(String roll2repeat) {
		Matcher limitDamageMatcher = limitDamagePattern.matcher(roll2repeat);
		return limitDamageMatcher.find();
	}

	private static Limits parseLimits(String roll) {
		
		int lowerLimit = Integer.MIN_VALUE;
		int upperLimit = Integer.MAX_VALUE;
		
		Matcher limitDamageMatcher = limitDamagePattern.matcher(roll);
		if (limitDamageMatcher.find()) {
			lowerLimit = calculateLowerLimit(roll);
			upperLimit = calculateUpperLimit(roll);
		}
		
		return new Limits(lowerLimit, upperLimit);
	}

	private static DiceRollResult parseSumm(String roll, Limits limits) throws ParseErrorException, WrongDieCodeException {
	
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
			leftRoll.add(parseSumm(right, Limits.NOLIMITS), sign);
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

		Matcher d66Matcher = d66RollPattern.matcher(roll);
		if (d66Matcher.find()) {
			return Dice.rollD66Dice();
		}
		
		Matcher fudgeMatcher = fudgePattern.matcher(roll);
		if (fudgeMatcher .find()) {
			Integer dieCount = 1;
			int pos = roll.trim().toLowerCase().indexOf("df");
			if (pos == -1) {
				pos = roll.trim().toLowerCase().indexOf("dF");
			}
			if (pos == -1) {
				pos = roll.trim().toLowerCase().indexOf("‰Ù");
			}
			if (pos == -1) {
				pos = roll.trim().toLowerCase().indexOf("ÍÙ");
			}			
			dieCount = Integer.parseInt(roll.substring(0, pos));
			return Dice.rollFudgeDice(dieCount);
		}
		
		Matcher basicExplodingMatcher = basicAndExplodingPattern.matcher(roll);
		if (basicExplodingMatcher.find()) {
			boolean explode = roll.endsWith("!");
			String dieLetter = findDieLetter(roll);			
			String[] s = roll.split(dieLetter);
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
			String dieLetter = findDieLetter(roll);
			String[] s = roll.split(dieLetter);
			Integer dieCount = 1;
			if (!s[0].trim().isEmpty() ) {
				dieCount = Integer.parseInt(s[0]);	
			}
			String keepLowest = findKeepLowest(roll);
			String[] d = s[1].split(keepLowest);
			Integer dieSize=Integer.parseInt(d[0]);
			Integer keepCount = Integer.parseInt(d[1]);
			if (keepCount>dieCount) {
				throw new ParseErrorException("Keep dice count must be no more than total dice count!");				
			}
			return Dice.rollRollKeepLowestDice(dieCount, dieSize, keepCount);
		}
		
		Matcher rollAndKeepMatcher = rollAndKeepPattern.matcher(roll);
		if(rollAndKeepMatcher.find()) {

			String[] s = roll.split(findDieLetter(roll));
			Integer dieCount = 1;
			if (!s[0].trim().isEmpty() ) {
				dieCount = Integer.parseInt(s[0]);	
			}
			String[] d = s[1].split(findKeepLetter(roll));
			Integer dieSize=Integer.parseInt(d[0]);
			Integer keepCount = Integer.parseInt(d[1]);
			if (keepCount>dieCount) {
				throw new ParseErrorException("Keep dice count must be no more than total dice count!");				
			}
			return Dice.rollRollKeepDice(dieCount, dieSize, keepCount);
		}

		Matcher savageWorldsPatternWithCustomWildDieMatcher = savageWorldsPatternWithCustomWildDiePattern.matcher(roll);
		if(savageWorldsPatternWithCustomWildDieMatcher.find()) {
			String[] s = roll.split(findSavageLetter(roll));
			int diceCount = 1;
			if (!s[0].trim().isEmpty() ) {
				diceCount = Integer.parseInt(s[0]);	
			}
			String[] s1 = s[1].split(findWildDieLetter(s[1]));
 
			int traitDieSize = Integer.parseInt(s1[0]);
			Integer wildDieSize = 6;
			if (!s1[1].trim().isEmpty() ) {
				wildDieSize = Integer.parseInt(s1[1]);	
			}
			return Dice.rollSavageDice(diceCount, traitDieSize, wildDieSize);
		}		
		
		Matcher savageWorldsMatcher = savageWorldsPattern.matcher(roll);
		if(savageWorldsMatcher.find()) {
			String[] s = roll.split(findSavageLetter(roll));
			int diceCount = 1;
			if (!s[0].trim().isEmpty() ) {
				diceCount = Integer.parseInt(s[0]);	
			}
			int traitDieSize = Integer.parseInt(s[1]);
			return Dice.rollSavageDice(diceCount, traitDieSize, 6);
		}
		
		Matcher ladyBlackbirdMatcher = ladyBlackbirdPattern.matcher(roll);
		if(ladyBlackbirdMatcher.find()) {
			String[] s = roll.split(findDieLetter(roll));
			Integer dieCount = 1;
			if (!s[0].trim().isEmpty() ) {
				dieCount = Integer.parseInt(s[0]);	
			}
			String[] d = s[1].split(findSuccessLetter(roll));
			Integer dieSize=Integer.parseInt(d[0]);
			Integer successTreshold = Integer.parseInt(d[1]);
			if (successTreshold>dieSize || successTreshold<2) {
				throw new ParseErrorException("Success threshold must be more than 1 and less than die size!");				
			}
			return Dice.rollLadyBlackbirdDice(dieCount, dieSize, successTreshold);
		}
		
		return new DicelessRollResult(roll);
	}

	private static int calculateUpperLimit(String roll) {
		
		
		return 0;
	}

	private static int calculateLowerLimit(String roll) {
		
		int lowerDieSize = findLowerDie(roll);
		
		return lowerDieSize;
	}

	private static int findLowerDie(String roll) {
	
		int dieSize = Integer.MAX_VALUE; 
		String[] items = roll.split("(\\+|\\-)");
		for (String item : items) {
			String[] s = item.split("d");
			if (s.length>1) {
				try {
					int val = Integer.parseInt(s[1]);
					if (val<dieSize) {
						dieSize = val;
					}
				} catch (NumberFormatException e) {
				}
			}
		}
		return dieSize;
	}

	private static String findWildDieLetter(String roll) {
		String[] klLetters = {"w", "W", "‰", "ƒ" };
		return findLetter(klLetters, roll);
	}

	private static String findSuccessLetter(String roll) {
		String[] klLetters = {"s", "S", "Û", "”" };
		return findLetter(klLetters, roll);
	}

	private static String findSavageLetter(String roll) {
		String[] klLetters = {"s", "S", "Ò", "—" };
		return findLetter(klLetters, roll);
	}

	private static String findKeepLetter(String roll) {
		
		String[] klLetters = {"k", "K", "·", "¡" };
		return findLetter(klLetters, roll);
	}

	private static String findKeepLowest(String roll) {
		String[] klLetters = {"kl", "KL", "Ï", "Ã" };
		
		return findLetter(klLetters, roll);
	}

	private static String findLetter(String[] letters, String roll) {
		for (String letter : letters) {
			if (roll.contains(letter)) {
				return letter;
			}
		}
		return null;
	}

	private static String findDieLetter(String roll) {
		
		String[] dieLetters = {"d", "D", "Í", " ", "‰", "ƒ"}; 
		
		return findLetter(dieLetters , roll);

	}
}
