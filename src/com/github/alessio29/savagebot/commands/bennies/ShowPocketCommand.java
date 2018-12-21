package com.github.alessio29.savagebot.commands.bennies;

import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.internal.Messages;
import com.github.alessio29.savagebot.savagebot.bennies.Pocket;
import com.github.alessio29.savagebot.savagebot.bennies.Pockets;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class ShowPocketCommand implements ICommand {

	@Override
	public String getName() {
		return "pocket";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.BENNIES;
	}

	@Override
	public String getDescription() {
		return "Shows character's bennies";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"<characterName>"};
		return res;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		if( args.length<1) {
			throw new Exception("No character name provided. Usage: ~benny <Character Name>"); 
		}
		String charName = Messages.createNameFromArgs(args, 0);
		Guild guild = event.getGuild();
		Channel channel = event.getTextChannel();
		Pocket pocket = Pockets.getPocket(guild, channel, charName);
		StringBuilder reply = new StringBuilder();
		reply.append(Messages.capitalize(charName)).append(" has in his pocket").append(pocket.getInfo()).append("\n");
		return new CommandExecutionResult(reply.toString(), 2);
	}
}
