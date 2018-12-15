package org.alessio29.savagebot.commands.initiative;

import org.alessio29.savagebot.internal.Messages;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;
import com.Cardinal.CommandPackage.Exceptions.MissingRequirementsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * 
 * @author aless
 * 
 * This command deals cards to PC or NPC
 * 
 * Syntax: init
 * 
 */


public class ShowInitiativeCommand implements ICommand {

	@Override
	public String getName() {
		return "init";
	}

	@Override
	public Category getCategory() {
		return Category.UTILITY;
	}

	@Override
	public String getDescription() {
		return "Shows initiative tracker";
	}

	@Override
	public String[] getArguments() {
		return null;	}
	
	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix)
			throws MissingRequirementsException, MissingArgumentsException {

		Messages.showOrder(event);
	}
	
}