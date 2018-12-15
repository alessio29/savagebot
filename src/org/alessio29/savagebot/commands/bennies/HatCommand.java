package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;
import org.alessio29.savagebot.bennies.Pockets;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class HatCommand implements ICommand{

	private static final String RESET = "fill";

	@Override
	public String getName() {
		return "hat";
	}

	@Override
	public Category getCategory() {
		return Category.CUSTOM;
	}

	@Override
	public String getDescription() {
		return "Puts all required bennies into the hat";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) {
		
		if (args.length>1) {
			Hats.getHat(event.getGuild(), event.getChannel(), args[1].equals(RESET));
			Pockets.resetPockets(event.getGuild(), event.getChannel());
		} else {
			Hats.getHat(event.getGuild(), event.getChannel(), false);
		}
		Hat hat = Hats.getHat(event.getGuild(), event.getChannel(), false);
		event.getChannel().sendMessage(hat.getInfo());
	}
}
