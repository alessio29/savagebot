package org.alessio29.savagebot.apiActions.cards;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class ShuffleCardsAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String [] args) {
        Deck deck = Decks.getDeck(message.getGuildId(), message.getChannelId());
        deck.shuffle();

        for (String userId : Hands.getHands(message.getGuildId(), message.getChannelId()).keySet()) {
            Hands.getHand(message.getGuildId(), message.getChannelId(), userId).clear();
//            Hands.saveHands();
        }
        return new CommandExecutionResult(new ReplyBuilder().newLine().attach("Shuffled...").newLine().toString(), 1);
    }
}
