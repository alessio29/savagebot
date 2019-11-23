package org.alessio29.savagebot.apiActions.admin;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.commands.CommandRegistry;

import java.util.*;
import java.util.stream.Collectors;

public class HelpAction implements IBotAction {

    private static final String README_LINK =
            "https://github.com/alessio29/savagebot/blob/master/README.md";

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
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
}
