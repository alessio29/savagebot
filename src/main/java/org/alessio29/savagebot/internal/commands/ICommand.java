package org.alessio29.savagebot.internal.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;

import java.util.List;

public interface ICommand {
	
	String getName();

	CommandCategory getCategory();
	
	String getDescription();
	
	String[] getAliases();
	
	String[] getArguments();
	
	CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception;

	default String asHelpString() {

		String name = ReplyBuilder.bold(this.getName());
		if (getAliases() != null) {
			List<String> aliases = ReplyBuilder.bold(getAliases());
			name += " or " + String.join(" or ", aliases);
		}
		name += "\t";
		if (getArguments() != null) {
			name += String.join(" ", getArguments());
		}
		return name + "\t" + this.getDescription();
	}

	
}
