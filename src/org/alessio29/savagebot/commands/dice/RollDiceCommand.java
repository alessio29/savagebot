package org.alessio29.savagebot.commands.dice;

import org.alessio29.savagebot.exceptions.ParseErrorException;
import org.alessio29.savagebot.exceptions.WrongDieCodeException;
import org.alessio29.savagebot.internal.Roll;
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
		
		Roll.execute(event, args, prefix);
	}
}
