package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.initiative.*;
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
        return new NewFightAction().doAction(message, args);
    }

    @CommandCallback(
            name = "round",
            description = "Starts new round: resets initiative tracker, shuffles deck, if joker was dealt on previous round. " +
                    "If '+' provided as parameter - new cards are dealto to characters according to their edges/hindrances. " +
                    "If provided character name preceded by '-' - this character removed from fight (dropped, left, ran away etc).",
            aliases = {"rd"},
            arguments = {"[+]", "[-<char_name>]"}
    )

    public static CommandExecutionResult round(IMessageReceived message, String[] args) {
        new NewRoundAction().doAction(message, args);
        return new ShowInitiativeAction().doAction(message, args);
    }

    @CommandCallback(
            name = "init",
            description = "Shows initiative tracker",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult init(IMessageReceived message, String[] args) {
        return new ShowInitiativeAction().doAction(message, args);
    }

    @CommandCallback(
            name = "drop",
            description = "Drops character out of fight",
            aliases = {},
            arguments = {"<character_name>"}
    )
    public static CommandExecutionResult drop(IMessageReceived message, String[] args) {
        return new DropCharacterAction().doAction(message, args);
    }

    @CommandCallback(
            name = "di",
            description = "Deal initiative card",
            aliases = {},
            arguments = { "<character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]" }
    )
    public static CommandExecutionResult dealInitCards(IMessageReceived message, String[] args) {
        new DealInitiativeCardsAction().doAction(message, args);
        return new ShowInitiativeAction().doAction(message, args);
    }
}
