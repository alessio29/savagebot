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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
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
