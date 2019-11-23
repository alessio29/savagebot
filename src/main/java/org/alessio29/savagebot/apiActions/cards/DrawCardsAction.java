package org.alessio29.savagebot.apiActions.cards;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class DrawCardsAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        Deck deck = Decks.getDeck(message.getGuildId(), message.getChannelId());
        if(deck.isEmpty()) {
            return new CommandExecutionResult("Shuffle is needed..", 2);
        }
        int count = 1;
        if (args.length > 0) {
            try {
                count = Integer.parseInt(args[0]);
            } catch (Exception e) {
                // count will be 1
            }
        }

        ReplyBuilder replyBuilder = new ReplyBuilder();
        for (int i=0; i<count;i++) {
            Card newCard = deck.getNextCard();
            if (newCard!=null) {
                Hands.getHand(message.getGuildId(), message.getChannelId(), message.getAuthorId()).getCards().add(newCard);
                replyBuilder.attach(newCard.toString()).space();
            } else {
                break;
            }
        }
        return new CommandExecutionResult(replyBuilder.toString(), 2, true);
    }
}
