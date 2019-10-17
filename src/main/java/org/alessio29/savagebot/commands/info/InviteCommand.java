package org.alessio29.savagebot.commands.info;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.commands.CommandCategory;
import org.alessio29.savagebot.internal.commands.ICommand;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

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
	public CommandCategory getCategory() {
		return CommandCategory.INFO;
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
