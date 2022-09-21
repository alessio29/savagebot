package org.alessio29.savagebot.internal.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.alessio29.savagebot.commands.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CommandRegistry {

	private static final CommandRegistry INSTANCE = new CommandRegistry();

	private final Map<String, ICommand> registeredCommands = new HashMap<>();
	private final List<IParsingCommand> parsingCommands = new ArrayList<>();
	private final Map<String, IDiscordCommand> discordCommands = new HashMap<>();
	private JDA jda;

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

	private static String[] getOptionNames(DiscordCommandCallback cb) {
		DiscordOption[] options = cb.options();
		int numOptions = options.length;
		String[] optionNames = new String[numOptions];
		for (int i = 0; i < numOptions; ++i) {
			optionNames[i] = options[i].name();
		}
		return optionNames;
	}

	public void registerCommandsFromMethods(Object methodOwner, Class<?> methodClass) {
		boolean shouldBeStatic = methodOwner == null;

		CommandCategoryOwner commandCategoryAnn = methodClass.getDeclaredAnnotation(CommandCategoryOwner.class);
		CommandCategory classCommandCategory = commandCategoryAnn != null ? commandCategoryAnn.value() : null;

		ArrayList<CommandData> discordSlashCommandsToRegister = new ArrayList<>();

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

			DiscordCommandCallback discordCommandCallback = method.getDeclaredAnnotation(DiscordCommandCallback.class);
			if (discordCommandCallback != null) {
				String name = discordCommandCallback.name();
				discordCommands.put(name, new DiscordMethodSlashCommand(
						name,
						method, methodOwner,
						discordCommandCallback.shouldDefer(),
						getOptionNames(discordCommandCallback)));
				discordSlashCommandsToRegister.add(makeSlashCommandData(discordCommandCallback));
			}
		}

		if (jda != null && !discordSlashCommandsToRegister.isEmpty()) {
			for (CommandData commandData : discordSlashCommandsToRegister) {
				System.out.println("Registering /-command: " + commandData.getName());
			}
			jda.updateCommands().addCommands(discordSlashCommandsToRegister).queue();
		}
	}

	private SlashCommandData makeSlashCommandData(DiscordCommandCallback dc) {
		SlashCommandData slash = Commands.slash(dc.name(), dc.description());
		DiscordOption[] options = dc.options();
		int numOptions = options.length;
		if (numOptions > 0) {
			OptionData[] optionData = new OptionData[numOptions];
			for (int i = 0; i < numOptions; ++i) {
				DiscordOption option = options[i];
				optionData[i] = new OptionData(option.optionType(), option.name(), option.description());
			}
			slash.addOptions(optionData);
		}
		return slash;
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

	public void reset() {
		registeredCommands.clear();
		parsingCommands.clear();
	}

	public void setJDA(JDA jda) {
		this.jda = jda;
	}

	public IDiscordCommand getDiscordCommand(String commandName) {
		return discordCommands.get(commandName);
	}
}
