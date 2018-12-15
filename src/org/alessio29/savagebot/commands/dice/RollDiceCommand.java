package org.alessio29.savagebot.commands.dice;

import org.alessio29.savagebot.exceptions.ParseErrorException;
import org.alessio29.savagebot.exceptions.WrongDieCodeException;
import org.alessio29.savagebot.parser.SimpleParser;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

public class RollDiceCommand implements ICommand {

	@Override
	public String getName() {

		return "roll";
	}

	@Override
	public Category getCategory() {
		
		return Category.UTILITY;
	}

	@Override
	public String getDescription() {
		return "roll or r = rolls dice\n"
				+ "Currently supported dice codes are:\n"
				+ "ndm - roll n m-sided dice, show sum\n"
				+ "ndm! - roll n 'exploding' m-sided dice, show sum\n"
				+ "ndmkp - roll n m-sided dice keep p highest\n"
				+ "ndmklp - roll n m-sided dice keep p lowest\n"
				+ "sm - where m is one of (4,6,8,10,12) - Savage Worlds roll with wild die\n"
				+ "nsm - as previous but instead of d6 wild die is dn\n"
				+ "kxdice - roll dice k times - i.e. 6x4d6k3\n"
				+ "You can use multiple rolls in one command separeted by space:\n"
				+ "~r d6 d4! d10+d12\n"
				+ "You can use comments in roll:\n"
				+ "~r shooting at vampire s8 damage 2d6+1\n";
	}

	@Override
	public String[] getArguments() {

		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {
		
		if (args.length<2) {
			throw new MissingArgumentsException("No die codes provided!");
		}
		
		String result = args[1];
		for (int i=2; i<args.length; i++) {
			result = result + " "+ args[i];
		}
		
		try {
			result = SimpleParser.parseString(result);
		} catch (ParseErrorException e) {
			throw new MissingArgumentsException("Can't understand roll: "+e.getMessage());
		} catch (WrongDieCodeException e) {
			throw new MissingArgumentsException("Can'roll: "+e.getMessage());
		}
		
		IChannel ch = event.getChannel();
		ch.sendMessage(event.getAuthor().mention()+" rolls: "+result);
	}
}