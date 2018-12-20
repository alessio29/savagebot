package com.github.alessio29.savagebot.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.alessio29.savagebot.commands.ICommand;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRegistry {

	private static CommandRegistry instance = new CommandRegistry();
	private static Map<String, ICommand> registeredCommands = new HashMap<>();
	
	public static void registerCommand(ICommand newCommand) {
		registeredCommands.put(newCommand.getName(), newCommand);
	}
	
	public static CommandRegistry current() {
		return instance;
	}

	public Collection<ICommand> getRegisteredCommands() {
		return registeredCommands.values();
	}

	public ICommand getCommandByName(String command) {
		return registeredCommands.get(command);
	}

	public CommandExecutionResult execute(MessageReceivedEvent event, String command, String[] args) throws Exception {

		ICommand cmd = getCommandByName(command);
		
		if (cmd == null) {
			return new CommandExecutionResult();
		}
		return cmd.execute(event, args);
	}
}
