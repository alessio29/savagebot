package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.commands.ICommand;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

	private static CommandRegistry instance = new CommandRegistry();
	private static Map<String, ICommand> registeredCommands = new HashMap<>();
	
	public static void registerCommand(ICommand newCommand) {
		registeredCommands.put(newCommand.getName(), newCommand);
		if (newCommand.getAliases()!=null) {
			for (String alias : newCommand.getAliases()) {
				registeredCommands.put(alias, newCommand);
			}
		}
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
