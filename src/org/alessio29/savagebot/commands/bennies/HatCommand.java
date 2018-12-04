package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class HatCommand implements ICommand{

	private static final String INFO = "info";

	@Override
	public String getName() {

		return "hat";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {

		return "Puts all bennies to the hat";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) {
		if (args.length>1 && args[1].equalsIgnoreCase(INFO)) {
			Hat hat = Hats.getHat(event.getGuild(), event.getChannel());
			event.getChannel().sendMessage(hat.getInfo());			
			return;
		}
		Hats.getHat(event.getGuild(), event.getChannel());
		event.getChannel().sendMessage("Filled hat with bennies...");
	}
}
