package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.tokens.ClearTokensAction;
import org.alessio29.savagebot.apiActions.tokens.GiveTokenAction;
import org.alessio29.savagebot.apiActions.tokens.ListTokensAction;
import org.alessio29.savagebot.apiActions.tokens.TakeTokenAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.TOKENS)
public class TokenCommands {


    @CommandCallback(
            name = "clear",
            description = "Clear tokens for character/all characters",
            aliases = {},
            arguments = {"<character_name>/all"}
    )
    public static CommandExecutionResult clear(MessageReceivedEvent event, String[] args) {
        return new ClearTokensAction().doAction(event, args);
    }

    @CommandCallback(
            name = "tokens",
            description = "List all characters and their tokens",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult list(MessageReceivedEvent event, String[] args) {
        return new ListTokensAction().doAction(event, args);
    }

    @CommandCallback(
            name = "take",
            description = "Take token(s) from character",
            aliases = {},
            arguments = {"<character_name>", "[<amount of tokens>]"}
    )
    public static CommandExecutionResult take(MessageReceivedEvent event, String[] args) {
        return new TakeTokenAction().doAction(event, args);
    }

    @CommandCallback(
            name = "give",
            description = "Give token(s) to character",
            aliases = {},
            arguments = {"<character_name>", "[<amount of tokens>]"}
    )
    public static CommandExecutionResult give(MessageReceivedEvent event, String[] args) {
        return new GiveTokenAction().doAction(event, args);
    }

}
