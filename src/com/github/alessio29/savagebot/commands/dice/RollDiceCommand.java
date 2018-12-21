package com.github.alessio29.savagebot.commands.dice;

import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.exceptions.ParseErrorException;
import com.github.alessio29.savagebot.exceptions.WrongDieCodeException;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.parser.SimpleParser;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class RollDiceCommand implements ICommand {

	@Override
	public String getName() {
		return "r";
	}

	@Override
	public String[] getAliases() {
		String[] res = {"roll"};
		return res;
	}
	
	@Override
	public Category getCategory() {
		return Category.DICE;
	}

	@Override
	public String getDescription() {
		return "rolls dice\n"
				+"You can use multiple rolls in one command separated by space: `.r d6 d4! d10+d12`\n"
				+"You can use comments in roll: `.r shooting at vampire s8 damage 2d6+1`";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"<die code1>", "<die code2>",  "...", "<die code n>"};
		return res;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws ParseErrorException {

		if (args.length<1) {
			throw new ParseErrorException("No die codes provided!");
		}
		
		String result = args[0];
		for (int i=1; i<args.length; i++) {
			result = result + " "+ args[i];
		}
		
		try {
			result = SimpleParser.parseString(result);
		} catch (ParseErrorException e) {
			throw new ParseErrorException("Can't understand roll: "+e.getMessage());
		} catch (WrongDieCodeException e) {
			throw new ParseErrorException("Can'roll: "+e.getMessage());
		}
		return new CommandExecutionResult(result, args.length+1);
	}
}