package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.commands.ICommand;

public class CommandInterpreter {

	public static CommandExecutionResult runCommand(MessageReceivedEvent event, String command, String[] args) throws Exception {
		ICommand cmd = CommandRegistry.current().getCommandByName(command);  
		if (cmd!=null) {
			return CommandRegistry.current().execute(event, command, args);	
		}
		return new CommandExecutionResult(command, 1);
	}
}
