package org.alessio29.savagebot.commands.bennies;

import org.alessio29.savagebot.bennies.Pocket;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.Messages;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;
import com.Cardinal.CommandPackage.Exceptions.MissingArgumentsException;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ShowPocketCommand implements ICommand {

	@Override
	public String getName() {
		return "pocket";
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "Shows what he's got...";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) throws MissingArgumentsException{

		if( args.length<2) {
			throw new MissingArgumentsException("No character name provided. Usage: ~benny <Character Name>"); 
		}
		String charName = Messages.createNameFromArgs(args, 1);
		IGuild guild = event.getGuild();
		Pocket pocket = Pockets.getPocket(guild, charName);
		StringBuilder reply = new StringBuilder();
		reply.append(Messages.capitalize(charName)).append(" has in his posket").append(pocket.getInfo()).append("\n");
		IChannel ch = event.getChannel();
		ch.sendMessage(reply.toString());
	}

}
