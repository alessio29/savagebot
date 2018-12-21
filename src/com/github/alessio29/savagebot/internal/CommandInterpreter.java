package com.github.alessio29.savagebot.internal;

import com.github.alessio29.savagebot.commands.ICommand;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandInterpreter {

	public static CommandExecutionResult runCommand(MessageReceivedEvent event, String command, String[] args) throws Exception {
		ICommand cmd = CommandRegistry.current().getCommandByName(command);  
		if (cmd!=null) {
			return CommandRegistry.current().execute(event, command, args);	
		}
		return new CommandExecutionResult(command, 1);
	}
}
