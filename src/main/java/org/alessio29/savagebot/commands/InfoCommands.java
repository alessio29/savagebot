package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.admin.HelpAction;
import org.alessio29.savagebot.apiActions.admin.InviteAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;


@CommandCategoryOwner(CommandCategory.INFO)
public class InfoCommands {

	@CommandCallback(
			name = "help",
			description = "Lists the description and syntax for registered commands.",
			aliases = {},
			arguments = {"[<command> or <category>]"}
	)
	public static CommandExecutionResult help(IMessageReceived message, String[] args) {
		return new HelpAction().doAction(args);
	}

	@CommandCallback(
			name = "invite",
			description = "Creates invite link for this bot",
			aliases = {},
			arguments = {}
	)
	public static CommandExecutionResult invite(IMessageReceived message, String[] args) {
		return new InviteAction().doAction();
	}
}
