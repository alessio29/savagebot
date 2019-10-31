package org.alessio29.savagebot.commands.info;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.SavageBotRunner;
import org.alessio29.savagebot.commands.CommandCallback;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.CommandCategoryOwner;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.commands.CommandRegistry;

import java.util.*;
import java.util.stream.Collectors;


@CommandCategoryOwner(CommandCategory.INFO)
public class InfoCommands {
	private static final String INVITE_LINK =
			"https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0";

	private static final String README_LINK =
			"https://github.com/alessio29/savagebot/blob/master/README.md";

	@CommandCallback(
			name = "help",
			description = "Lists the description and syntax for registered commands.",
			aliases = {},
			arguments = {"[<command> or <category>]"}
	)
	public static CommandExecutionResult help(MessageReceivedEvent event, String[] args) {
		if (args.length == 0) {
			return new CommandExecutionResult(getBriefHelpForAllCommands(), 1, true);
		}

		String arg0 = args[0];

		ICommand command = CommandRegistry.getInstance().getCommandByName(arg0);
		if (command != null) {
			return new CommandExecutionResult(getHelpForCommand(command), 2, true);
		}

		CommandCategory category = CommandCategory.valueOfOrNull(arg0.toUpperCase(Locale.US));
		if (category != null) {
			return new CommandExecutionResult(getHelpForCategory(category), 2, true);
		}

		// fallback, I don't know such command or category
		return new CommandExecutionResult(getBriefHelpForAllCommands(), 1, true);
	}

	public static String getBriefHelpForAllCommands() {
		Map<CommandCategory, List<ICommand>> byCategory =
				CommandRegistry.getInstance().getRegisteredCommands().stream()
						.collect(Collectors.groupingBy(ICommand::getCategory));

		ReplyBuilder replyBuilder = new ReplyBuilder();

		for (CommandCategory category : CommandCategory.values()) {
			Collection<ICommand> commands = byCategory.get(category);
			if (commands == null) continue;

			appendCategoryHeader(replyBuilder, category);
			appendBriefHelpForCommands(replyBuilder, new HashSet<>(commands));

			replyBuilder.newLine();
		}

		replyBuilder.attach("For more details, use `!help <command>` or see ").attach(README_LINK);

		return replyBuilder.toString();
	}

	private static void appendCategoryHeader(ReplyBuilder replyBuilder, CommandCategory category) {
		replyBuilder
				.attach(ReplyBuilder.underlined(ReplyBuilder.bold(category.toString() + " category")))
				.newLine();
	}

	private static void appendBriefHelpForCommands(ReplyBuilder replyBuilder, Collection<ICommand> commands) {
		commands.stream()
				.sorted(Comparator.comparing(ICommand::getName))
				.forEach(
						command -> replyBuilder.attach(getBriefHelp(command)).newLine()
				);
	}

	private static String getBriefHelp(ICommand command) {
		StringBuilder result = new StringBuilder();
		result.append("!");
		result.append(command.getName());

		String[] arguments = command.getArguments();
		if (arguments != null && arguments.length > 0) {
			result.append(" ");
			result.append(String.join(" ", arguments));
		}

		String[] aliases = command.getAliases();
		if (aliases != null && aliases.length > 0) {
			result.append("; aliases:");
			for (String alias : aliases) {
				result.append(" !");
				result.append(alias);
			}
		}

		return result.toString();
	}

	private static String getHelpForCommand(ICommand command) {
		return getHelp(command);
	}

	private static String getHelpForCategory(CommandCategory category) {
		Set<ICommand> commands = CommandRegistry.getInstance().getRegisteredCommands().stream()
				.filter(command -> command.getCategory() == category)
				.collect(Collectors.toSet());

		ReplyBuilder replyBuilder = new ReplyBuilder();
		appendCategoryHeader(replyBuilder, category);
		appendBriefHelpForCommands(replyBuilder, commands);
		return replyBuilder.toString();
	}

	private static String getHelp(ICommand command) {
		String name = ReplyBuilder.bold(command.getName());
		String[] aliases1 = command.getAliases();
		if (aliases1 != null && aliases1.length > 0) {
			List<String> aliases = ReplyBuilder.bold(aliases1);
			name += " or " + String.join(" or ", aliases);
		}
		name += "\t";
		if (command.getArguments() != null) {
			name += String.join(" ", command.getArguments());
		}
		return name + "\t" + command.getDescription();
	}

	@CommandCallback(
			name = "invite",
			description = "Creates invite link for this bot",
			aliases = {},
			arguments = {}
	)
	public static CommandExecutionResult invite(MessageReceivedEvent event, String[] args) {
		return new CommandExecutionResult("Invite link: " + INVITE_LINK + "\n", 1);
	}


//	@CommandCallback(
//			name = "info",
//			description = "Shows statistic about bot",
//			aliases = {},
//			arguments = {}
//	)
//	public static CommandExecutionResult info(MessageReceivedEvent event, String[] args) {
//
//		CommandExecutionResult result = null;
//		if (args.length>0) {
//			String password = args[0].trim();
//			if (SavageBotRunner.passwdOk(password)) {
//				int serversCount = event.getJDA().getGuilds().size();
//				result = new CommandExecutionResult("Bot registered at "+serversCount + " servers.", 2);
//			}
//		} else {
//			result = new CommandExecutionResult("Password must be provided!", 1);
//		}
//		return result;
//	}

}
