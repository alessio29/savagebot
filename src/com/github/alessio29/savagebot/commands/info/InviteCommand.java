package com.github.alessio29.savagebot.commands.info;

import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InviteCommand implements ICommand {

	@Override
	public String getName() {
		return "invite";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.INFO;
	}

	@Override
	public String getDescription() {
		return "Creates invite link for this bot";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
		return new CommandExecutionResult("Invite Link: https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0\n", 1);
	}

}
