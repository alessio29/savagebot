package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.admin.HelpAction;
import org.alessio29.savagebot.apiActions.admin.InviteAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;


@CommandCategoryOwner(CommandCategory.INFO)
public class InfoCommands {

	@CommandCallback(
			name = "help",
			description = "Lists the description and syntax for registered commands.",
			aliases = {},
			arguments = {"[<command> or <category>]"}
	)
	public static CommandExecutionResult help(MessageReceivedEvent event, String[] args) {
		return new HelpAction().doAction(args);
	}

	@CommandCallback(
			name = "invite",
			description = "Creates invite link for this bot",
			aliases = {},
			arguments = {}
	)
	public static CommandExecutionResult invite(MessageReceivedEvent event, String[] args) {
		return new InviteAction().doAction();
	}
}
