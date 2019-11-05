package org.alessio29.savagebot.apiActions.cards;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.List;

public class ShuffleCardsAction {

    public CommandExecutionResult doAction(List<String> users, String guildId, String channelId, String [] args) {
        Deck deck = Decks.getDeck(guildId, channelId);
        deck.shuffle();

        for (String userId : users) {
            Hands.getHand(guildId, userId).clear();
            Hands.saveHands();
        }
        return new CommandExecutionResult(new ReplyBuilder().newLine().attach("Shuffled...").newLine().toString(), 1);
    }
}
