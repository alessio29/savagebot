package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.cards.DealCardsAction;
import org.alessio29.savagebot.apiActions.cards.PutCardsAction;
import org.alessio29.savagebot.apiActions.cards.ShowCardsAction;
import org.alessio29.savagebot.apiActions.cards.ShuffleCardsAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.CARDS)
public class CardCommands {

    @CommandCallback(
            name = "put",
            description = "puts on table (to current channel) several (1 by default) cards",
            aliases = {},
            arguments = {"[<card_count>]"}
    )
    public static CommandExecutionResult open(IMessageReceived message, String[] args) {
        return new PutCardsAction().doAction(message, args);
    }

    @CommandCallback(
            name = "show",
            description = "Shows your cards, previously dealt to you by 'deal' command to current channel",
            aliases = {"sh"},
            arguments = {}
    )
    public static CommandExecutionResult show(IMessageReceived message, String[] args) {
        return new ShowCardsAction().doAction(message, args);
    }

    @CommandCallback(
            name = "deal",
            description = "Secretly deals several (1 by default) cards to user (to self by default)",
            aliases = {"dl"},
            arguments = {"[<card_count>]", "[<user>]"}
    )
    public static CommandExecutionResult draw(IMessageReceived message, String[] args) {
        return new DealCardsAction().doAction(message, args);
    }

    @CommandCallback(
            name = "shuffle",
            description = "Shuffles current deck, resets secret cards dealt to all users in this channel",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult shuffle(IMessageReceived<MessageReceivedEvent> message, String[] args) {

        return new ShuffleCardsAction().doAction(message, args);
    }

}
