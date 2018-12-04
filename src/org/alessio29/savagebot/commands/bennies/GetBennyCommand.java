package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.Benny;
import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;
import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.Messages;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class GetBennyCommand implements ICommand{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "benny";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "Get benny from hat";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) throws MissingArgumentsException {

		if(args.length<2) {
			throw new MissingArgumentsException("No character name provided. Usage: ~benny <Character>");
		}
		
		IGuild guild = event.getGuild();
		IChannel channel = event.getChannel();
		IUser user = event.getAuthor();
		Hat hat = Hats.getHat(guild, channel);
		String charName = Messages.createNameFromArgs(args, 1);
		Pocket pocket = Pockets.getPocket(guild, charName);
		Benny benny = hat.getBenny();
		if (benny == null ) {
			event.getChannel().sendMessage("Hat is empty..");
			return;
		}

		pocket.put(benny);
		StringBuilder reply = new StringBuilder();
		reply.append(user.mention()).append(" got from hat ").append(benny.getColor()).append(" benny for ").append(Messages.capitalize(charName)).append(".\n");
		IChannel ch = event.getChannel();
		ch.sendMessage(reply.toString());
	}


}
