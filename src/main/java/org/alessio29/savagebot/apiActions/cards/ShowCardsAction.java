package org.alessio29.savagebot.apiActions.cards;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Hand;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class ShowCardsAction {

    public CommandExecutionResult doAction(String guildId, String userId, String[] args) {
        Hand hand = Hands.getHand(guildId, userId);
        ReplyBuilder replyBuilder = new ReplyBuilder();
        for (Card card : hand.getCards()) {
            replyBuilder.space().attach(card.toString());
        }
        if(replyBuilder.toString().trim().isEmpty()) {
            return new CommandExecutionResult("Nothing to show..", 1);
        }
        return new CommandExecutionResult(replyBuilder.toString(), 1);
    }
}
