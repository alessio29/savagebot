package org.alessio29.savagebot.commands.initiative;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;


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
	public CommandCategory getCategory() {
		return CommandCategory.INITIATIVE;
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
		
		return new CommandExecutionResult(ReplyBuilder.showOrder(event), 1);
	}
	
}