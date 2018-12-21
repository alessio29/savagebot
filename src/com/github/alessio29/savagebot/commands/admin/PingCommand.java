package com.github.alessio29.savagebot.commands.admin;

import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.internal.Messages;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingCommand implements ICommand{
	
	public PingCommand() {
	}

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public String getDescription() {
		
		return "Checks SavageBot readiness";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
		return new CommandExecutionResult("Hey, "+Messages.mention(event.getAuthor())+" SavageBot is ready!\n", 1);
	}

}
