package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.internal.CommandExecutionResult;
import org.alessio29.savagebot.internal.Messages;

import java.util.List;

public interface ICommand {
	
	String getName();

	Category getCategory();
	
	String getDescription();
	
	String[] getAliases();
	
	String[] getArguments();
	
	CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception;

	default String asHelpString() {

		String name = Messages.bold(this.getName());
		if (getAliases()!=null) {
			List<String> aliases = Messages.bold(getAliases());
			name += " or " + String.join(" or ", aliases);
		}
		name +="\t";
		if (getArguments()!=null) {
			name += String.join(" ", getArguments());
		}
		return  name + "\t" + this.getDescription() ;
	}

	
}
