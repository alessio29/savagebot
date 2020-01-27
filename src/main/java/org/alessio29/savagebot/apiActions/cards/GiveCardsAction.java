package org.alessio29.savagebot.apiActions.cards;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class GiveCardsAction implements IBotAction {

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length <1 ) {
            return new CommandExecutionResult("Provide character name!", 1);
        }
        String charName = args[0];
        Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName);
        Deck deck = Decks.getDeck(message.getGuildId(), message.getChannelId());
        DrawCardResult cards = deck.getCardByParams("");
        character.giveCard(cards);
        return new CommandExecutionResult("Card " +cards.getCards().toString()+
                " given to character "+character.getName(), args.length + 1);
    }
}
