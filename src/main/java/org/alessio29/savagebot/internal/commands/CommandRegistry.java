package org.alessio29.savagebot.internal.commands;

import java.util.*;

public class CommandRegistry {

	private static final CommandRegistry INSTANCE = new CommandRegistry();

	private final Map<String, ICommand> registeredCommands = new HashMap<>();
	private final List<IParsingCommand> parsingCommands = new ArrayList<>();
	
	public void registerCommand(ICommand newCommand) {
		if (newCommand.getAliases() != null) {
			for (String alias : newCommand.getAliases()) {
				registeredCommands.put(alias, newCommand);
			}
		}
		if (newCommand instanceof IParsingCommand) {
			parsingCommands.add((IParsingCommand) newCommand);
		}
		registeredCommands.put(newCommand.getName(), newCommand);
	}
	
	public static CommandRegistry getInstance() {
		return INSTANCE;
	}

	public Collection<ICommand> getRegisteredCommands() {
		return registeredCommands.values();
	}

	public Collection<IParsingCommand> getRegisteredParsingCommands() {
		return parsingCommands;
	}

	public ICommand getCommandByName(String command) {
		return registeredCommands.get(command);
	}
}
