package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.initiative.*;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.INITIATIVE)
public class InitCommands {

    // TODO redesign initiative commands for Discord /-command interface

    @CommandCallback(
            name = "fight",
            description = "Starts new fight: shuffles deck, resets initiative tracker",
            aliases = {"f"},
            arguments = {}
    )
    @DiscordCommandCallback(
            name = "fight",
            description = "Starts new fight: shuffles deck, resets initiative tracker"
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
    @DiscordCommandCallback(
            name = "round",
            description = "Starts a new round: resets initiative tracker, shuffles deck, if joker was dealt on previous round."
    )
    public static CommandExecutionResult round(IMessageReceived message, String[] args) {
        return new NewRoundAction().doAction(message, args);
    }

    @CommandCallback(
            name = "init",
            description = "Shows initiative tracker",
            aliases = {},
            arguments = {}
    )
    @DiscordCommandCallback(
            name = "init",
            description = "Shows initiative tracker"
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
    @DiscordCommandCallback(
            name = "drop",
            description = "Drops character out of fight",
            options = {
                    @DiscordOption(name = "character", description = "name of the character to drop")
            }
    )
    public static CommandExecutionResult drop(IMessageReceived message, String[] args) {
        return new DropCharacterAction().doAction(message, args);
    }

    @CommandCallback(
            name = "card",
            description = "gives one more card to character",
            aliases = {"cd"},
            arguments = { "<character_name>"}
    )
    @DiscordCommandCallback(
            name = "drop",
            description = "gives one more card to character",
            options = {
                    @DiscordOption(name = "character", description = "name of the character to give one more card")
            }
    )
    public static CommandExecutionResult card(IMessageReceived message, String[] args) {
        return new GiveCardsAction().doAction(message, args);
    }

    @CommandCallback(
            name = "deal",
            description = "Deal initiative card",
            aliases = {"di"},
            arguments = {"<character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]"}
    )
    @DiscordCommandCallback(
            name = "deal",
            description = "Deal initiative card",
            varargOptionName = "arguments",
            varargOptionDescription = "<character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]"
    )
    public static CommandExecutionResult dealInitCards(IMessageReceived message, String[] args) {
        return new DealInitiativeCardsAction().doAction(message, args);
    }

    @CommandCallback(
            name = "hold",
            description = "Puts character on hold or return it to fight",
            aliases = {},
            arguments = { "[-]<character>" }
    )
    @DiscordCommandCallback(
            name = "hold",
            description = "Puts character on hold or return it to fight",
            options = {
                    @DiscordOption(name = "[-]character", description = "[-]character")
            }
    )
    public static CommandExecutionResult hold(IMessageReceived message, String[] args) {
        return new HoldAction().doAction(message, args);
    }
}
