package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.GiveCardsParamIterator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GiveCardsAction implements IBotAction {

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length <1 ) {
            return new CommandExecutionResult("Provide character name!", 1);
        }
        List<String> list = new ArrayList<>();
        Deck deck = Decks.getDeck(message.getGuildId(), message.getChannelId());
        GiveCardsParamIterator it = new GiveCardsParamIterator(args);

        while (it.hasNext()) {
            String value = it.next();
            if (it.isEntity(value)) {
                Character character = Characters.getByNameOrCreate(message.getGuildId(), message.getChannelId(), value);
                DrawCardResult cards = deck.getCardByParams("");
                character.giveCard(cards);
                character.backToFight();
                list.add(it.process(null, character));
            }
        }
        return new CommandExecutionResult(StringUtils.join(list, ", "), args.length + 1);
    }
}
