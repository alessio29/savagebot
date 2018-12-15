package org.alessio29.savagebot.commands;

import com.Cardinal.CommandPackage.Commands.Category;
import com.Cardinal.CommandPackage.Commands.ICommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class PingCommand implements ICommand{
	
	public PingCommand() {
		
	}
	

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public Category getCategory() {
		
		return Category.MISC;
	}

	@Override
	public String getDescription() {
		
		return "Use it to check whether bot is alive";
	}

	@Override
	public String[] getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MessageReceivedEvent event, String[] args, String prefix) {
		
		event.getChannel().sendMessage("Hey, "+event.getAuthor().mention()+" SavageBot is ready!");
		
	}

}
