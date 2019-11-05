package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.initiative.DealInitiativeCardsAction;
import org.alessio29.savagebot.apiActions.initiative.NewFightAction;
import org.alessio29.savagebot.apiActions.initiative.NewRoundAction;
import org.alessio29.savagebot.apiActions.initiative.ShowInitiativeAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.INITIATIVE)
public class InitCommands {

    @CommandCallback(
            name = "fight",
            description = "Starts new fight: shuffles deck, resets initiative tracker",
            aliases = {"f"},
            arguments = {}
    )
    public static CommandExecutionResult fight(MessageReceivedEvent event, String[] args) {
        return new NewFightAction().doAction(event.getAuthor().getId(), event.getGuild().getId(), event.getChannel().getId(), args);
    }

    @CommandCallback(
            name = "round",
            description = "Starts new round: resets initiative tracker, shuffles deck, if joker was dealt on previous round",
            aliases = {"rd"},
            arguments = {}
    )
    public static CommandExecutionResult round(MessageReceivedEvent event, String[] args) {
        return new NewRoundAction().doAction(event.getAuthor().getId(), event.getGuild().getId(), args);
    }

    @CommandCallback(
            name = "init",
            description = "Shows initiative tracker",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult init(MessageReceivedEvent event, String[] args) {
        return new ShowInitiativeAction().doAction(event.getGuild().getId(), args);
    }

    @CommandCallback(
            name = "di",
            description = "Deal initiative card",
            aliases = {},
            arguments = { "<character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]" }
    )
    public static CommandExecutionResult dealInitCards(MessageReceivedEvent event, String[] args) {
        new DealInitiativeCardsAction().doAction(event.getGuild().getId(), event.getChannel().getId(), args);
        return new ShowInitiativeAction().doAction(event.getGuild().getId(), args);
    }
}
