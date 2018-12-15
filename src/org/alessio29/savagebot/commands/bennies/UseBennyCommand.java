package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.Messages;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class UseBennyCommand implements ICommand {

	@Override
	public String getName() {
		return "use";
	}

	@Override
	public Category getCategory() {
		return Category.CUSTOM;
	}

	@Override
	public String getDescription() {
		return "Uses one of character's benny";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) {

		if(args.length <3) {
			event.getChannel().sendMessage("Cannot uderstand - must be 'use <BennyColor> <CharName>'.");
			return;
		}

		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		String charName = Messages.createNameFromArgs(args, 2);
		Pocket pocket = Pockets.getPocket(guild, channel, charName);
		BennyColor color = BennyColor.getColor(args[1]);
		if (color == null ) {
			event.getChannel().sendMessage("Something wrong with benny color.");
			return;
		}
		if (pocket.use(color)) {
			StringBuilder reply = new StringBuilder();
			reply.append(Messages.capitalize(charName)).append(" used ").append(color).append(" benny.\n");
			IChannel ch = event.getChannel();
			ch.sendMessage(reply.toString());
			return;
		}
		event.getChannel().sendMessage(charName+" has no such benny");
	}

}
