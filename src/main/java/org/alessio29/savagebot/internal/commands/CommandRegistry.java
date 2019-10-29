package org.alessio29.savagebot.internal.commands;

import org.alessio29.savagebot.commands.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
			registerParsingCommand((IParsingCommand) newCommand);
		}
		registeredCommands.put(newCommand.getName(), newCommand);
	}

	private void registerParsingCommand(IParsingCommand newCommand) {
		parsingCommands.add(newCommand);
	}

	public void registerCommandsFromStaticMethods(Class<?> methodClass) {
		registerCommandsFromMethods(null, methodClass);
	}

	public void registerCommandsFromMethods(Object methodOwner) {
		registerCommandsFromMethods(methodOwner, methodOwner.getClass());
	}

	public void registerCommandsFromMethods(Object methodOwner, Class<?> methodClass) {
		boolean shouldBeStatic = methodOwner == null;

		CommandCategoryOwner commandCategoryAnn = methodClass.getDeclaredAnnotation(CommandCategoryOwner.class);
		CommandCategory classCommandCategory = commandCategoryAnn != null ? commandCategoryAnn.value() : null;

		for (Method method : methodClass.getDeclaredMethods()) {
			if (shouldBeStatic != Modifier.isStatic(method.getModifiers())) continue;

			CommandCallback commandAnn = method.getDeclaredAnnotation(CommandCallback.class);
			if (commandAnn != null) {
				registerCommand(new MethodCommand(
						commandAnn.name(),
						classCommandCategory != null ? classCommandCategory : commandAnn.category(),
						commandAnn.description(),
						commandAnn.aliases(),
						commandAnn.arguments(),
						methodOwner,
						method
				));
			}

			ParsingCommandCallback parsingCommandAnn = method.getDeclaredAnnotation(ParsingCommandCallback.class);
			if (parsingCommandAnn != null) {
				registerParsingCommand(new ParsingMethodCommand(methodOwner, method));
			}
		}
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
