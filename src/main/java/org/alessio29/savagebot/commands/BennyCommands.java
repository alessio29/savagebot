package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.bennies.GiveBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.InitBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.ShowBenniesAction;
import org.alessio29.savagebot.apiActions.bennies.TakeBenniesAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.BENNIES)
public class BennyCommands {

    @CommandCallback(
            name = "hat",
            description = "Initializes bennies storage",
            aliases = {},
            arguments = {"[fill]"}
    )
    public static CommandExecutionResult init(IMessageReceived message, String[] args) {
        if (message.isPrivateMessage()) {
            // This just threw NPE originally
            throw new RuntimeException("No bennies for private channels");
        }
        return new InitBenniesAction().doAction(message.getGuildId(), message.getChannelId(), args);
    }

    @CommandCallback(
            name = "pocket",
            description = "Show character's bennies",
            aliases = {},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult show(IMessageReceived message, String[] args) {
        return new ShowBenniesAction().doAction(message.getGuildId(), message.getChannelId(), args);
    }

    @CommandCallback(
            name = "use",
            description = "Give new benny to character",
            aliases = {},
            arguments = {"<benny_color>", "<character_name>"}
    )
    public static CommandExecutionResult take(IMessageReceived message, String[] args) {
        return new TakeBenniesAction().doAction(message.getGuildId(), message.getChannelId(), args);
    }


    @CommandCallback(
            name = "benny",
            description = "Give new benny to character",
            aliases = {},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult give(IMessageReceived message, String[] args) {
        return new GiveBenniesAction().doAction(message.getGuildId(), message.getChannelId(), args);
    }
}
