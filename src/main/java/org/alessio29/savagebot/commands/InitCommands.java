package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.initiative.DealInitiativeCardsAction;
import org.alessio29.savagebot.apiActions.initiative.NewFightAction;
import org.alessio29.savagebot.apiActions.initiative.NewRoundAction;
import org.alessio29.savagebot.apiActions.initiative.ShowInitiativeAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.INITIATIVE)
public class InitCommands {

    @CommandCallback(
            name = "fight",
            description = "Starts new fight: shuffles deck, resets initiative tracker",
            aliases = {"f"},
            arguments = {}
    )
    public static CommandExecutionResult fight(IMessageReceived message, String[] args) {
        return new NewFightAction().doAction(message.getAuthorId(), message.getGuildId(), message.getChannelId(), args);
    }

    @CommandCallback(
            name = "round",
            description = "Starts new round: resets initiative tracker, shuffles deck, if joker was dealt on previous round",
            aliases = {"rd"},
            arguments = {}
    )
    public static CommandExecutionResult round(IMessageReceived message, String[] args) {
        return new NewRoundAction().doAction(message.getAuthorId(), message.getGuildId(), args);
    }

    @CommandCallback(
            name = "init",
            description = "Shows initiative tracker",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult init(IMessageReceived message, String[] args) {
        return new ShowInitiativeAction().doAction(message.getGuildId(), args);
    }

    @CommandCallback(
            name = "di",
            description = "Deal initiative card",
            aliases = {},
            arguments = { "<character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]" }
    )
    public static CommandExecutionResult dealInitCards(IMessageReceived message, String[] args) {
        new DealInitiativeCardsAction().doAction(message.getGuildId(), message.getChannelId(), args);
        return new ShowInitiativeAction().doAction(message.getGuildId(), args);
    }
}
