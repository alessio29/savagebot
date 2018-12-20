package com.github.alessio29.savagebot.commands.bennies;

import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.internal.Messages;
import com.github.alessio29.savagebot.savagebot.bennies.Benny;
import com.github.alessio29.savagebot.savagebot.bennies.Hat;
import com.github.alessio29.savagebot.savagebot.bennies.Hats;
import com.github.alessio29.savagebot.savagebot.bennies.Pocket;
import com.github.alessio29.savagebot.savagebot.bennies.Pockets;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class GetBennyCommand implements ICommand{

	@Override
	public String getName() {
		return "benny";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.BENNIES;
	}

	@Override
	public String getDescription() {
		return "Get benny from hat and adds it to characker's pocket";
	}

	@Override
	public String[] getArguments() {
		String[] res = {"<character>"}; 
		return res;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		if(args.length<1) {
			throw new Exception("No character name provided. Usage: ~benny <Character>");
		}
		
		Guild guild = event.getGuild();
		Channel channel = event.getTextChannel();
		User user = event.getAuthor();
		Hat hat = Hats.getHat(guild, channel, false);
		String charName = Messages.createNameFromArgs(args, 0);
		Pocket pocket = Pockets.getPocket(guild, channel, charName);
		Benny benny = hat.getBenny();
		if (benny == null ) {
			event.getChannel().sendMessage("Hat is empty..");
			return new CommandExecutionResult();
		}
		pocket.put(benny);
		StringBuilder reply = new StringBuilder();
		reply.append(user.getAsMention()).append(" got from hat ").append(benny.getColor()).append(" benny for ").append(Messages.capitalize(charName)).append(".\n");
		
		return new CommandExecutionResult(reply.toString(), 2);
	}


}
