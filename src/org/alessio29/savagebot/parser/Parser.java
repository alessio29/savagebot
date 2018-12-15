package org.alessio29.savagebot.parser;

import java.util.HashMap;

import org.alessio29.savagebot.exceptions.ParseErrorException;

public class Parser {
/*
	private HashMap<String, Integer> variables;

	public ParserExample() {
		variables = new HashMap<String, Integer>();
	}

	public void setVariable(String variableName, Integer variableValue) {
		variables.put(variableName, variableValue);
	}

	public Integer getVariable(String variableName)
	{
		return variables.containsKey(variableName)?variables.get(variableName):0;
	}

	public int parse(String s) throws Exception
	{
		ParseResult result = plusMinus(s);
		if (!result.getRemainder().isEmpty()) {
			throw new ParseErrorException("Parse error, unparsed remainder: "+result.getRemainder());
		}
		return result.getAccumulator();
	}

	private ParseResult plusMinus(String s) throws Exception
	{
		ParseResult current = multDiv(s);
		int acc = current.getAccumulator();

		while (current.getRemainder().length() > 0) {
			if (!(current.getRemainder().charAt(0) == '+' || current.getRemainder().charAt(0) == '-')) break;

			char sign = current.getRemainder().charAt(0);
			String next = current.getRemainder().substring(1);

			current = multDiv(next);
			if (sign == '+') {
				acc += current.getAccumulator();
			} else {
				acc -= current.getAccumulator();
			}
		}
		return new ParseResult(acc, current.getRemainder());
	}

	private ParseResult bracket(String s) throws Exception
	{
		char zeroChar = s.charAt(0);
		if (zeroChar == '(') {
			ParseResult r = plusMinus(s.substring(1));
			if (!r.getRemainder().isEmpty() && r.getRemainder().charAt(0) == ')') {
				r.setRemainder(r.getRemainder().substring(1));
			} else {
				System.err.println("Error: no closing bracket");
			}
			return r;
		}
		return functionVariable(s);
	}

	private ParseResult functionVariable(String s) throws Exception
	{
		String f = "";
		int i = 0;
		// ищем название функции или переменной
		// имя обязательно должна начинаться с буквы
		while (i < s.length() && (Character.isLetter(s.charAt(i)) || ( Character.isDigit(s.charAt(i)) && i > 0 ) )) {
			f += s.charAt(i);
			i++;
		}
		if (!f.isEmpty()) { // если что-нибудь нашли
			if ( s.length() > i && s.charAt( i ) == '(') { // и следующий символ скобка значит - это функция
				ParseResult r = bracket(s.substring(f.length()));
				return processFunction(f, r);
			} else { // иначе - это переменная
				return new ParseResult(getVariable(f), s.substring(f.length()));
			}
		}
		return number(s);
	}

	private ParseResult multDiv(String s) throws Exception
	{
		ParseResult current = bracket(s);

		int acc = current.getAccumulator();
		while (true) {
			if (current.getRemainder().length() == 0) {
				return current;
			}
			char sign = current.getRemainder().charAt(0);
			if ((sign != '*' && sign != '/')) return current;

			String next = current.getRemainder().substring(1);
			ParseResult right = bracket(next);

			if (sign == '*') {
				acc *= right.getAccumulator();
			} else {
				acc /= right.getAccumulator();
			}

			current = new ParseResult(acc, right.getRemainder());
		}
	}

	private ParseResult number(String s) throws Exception
	{
		int i = 0;

		boolean negative = false;
		// число также может начинаться с минуса
		if( s.charAt(0) == '-' ){
			negative = true;
			s = s.substring( 1 );
		}
		// разрешаем только цифры
		while (i < s.length() && (Character.isDigit(s.charAt(i)))) {
			i++;
		}
		if( i == 0 ){ // что-либо похожее на число мы не нашли
			throw new Exception( "can't get valid number in '" + s + "'" );
		}

		int intPart = Integer.parseInt(s.substring(0, i));
		if( negative ) intPart = -intPart;
		String restPart = s.substring(i);

		return new ParseResult(intPart, restPart);
	}

	// Тут определяем все нашие функции, которыми мы можем пользоватся в формулах
	private ParseResult processFunction(String func, ParseResult r)
	{
//		if (func.equals("sin")) {
//			return new ParseResult(Math.sin(Math.toRadians(r.getAccumulator())), r.getRemainder());
//		} else if (func.equals("cos")) {
//			return new ParseResult(Math.cos(Math.toRadians(r.getAccumulator())), r.getRemainder());
//		} else if (func.equals("tan")) {
//			return new ParseResult(Math.tan(Math.toRadians(r.getAccumulator())), r.getRemainder());
//		} else {
//			System.err.println("function '" + func + "' is not defined");
//		}
		return r;
	}
	*/
}

