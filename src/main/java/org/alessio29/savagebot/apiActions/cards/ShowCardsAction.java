package org.alessio29.savagebot.apiActions.cards;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Hand;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class ShowCardsAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        Hand hand = Hands.getHand(event.getGuild(), event.getAuthor());
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
