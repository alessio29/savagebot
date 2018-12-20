package com.github.alessio29.savagebot.commands.initiative;



import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.internal.Messages;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;



public class ShowInitiativeCommand implements ICommand {

	@Override
	public String getName() {
		return "init";
	}

	@Override
	public String[] getAliases() {
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.INITIATIVE;
	}

	@Override
	public String getDescription() {
		return "Shows initiative tracker";
	}

	@Override
	public String[] getArguments() {
		return null;	}
	

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		
		return new CommandExecutionResult(Messages.showOrder(event), 1);
	}
	
}