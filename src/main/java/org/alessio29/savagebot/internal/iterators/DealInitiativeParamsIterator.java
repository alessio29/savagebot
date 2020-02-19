package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.characters.Character;

public class DealInitiativeParamsIterator extends ParamsIterator {

    private Deck deck;

    public DealInitiativeParamsIterator(String[] args, Deck deck) {
        super(args);
        this.deck = deck;
    }

    @Override
    public boolean isModifier(String param) {
        return param.trim().startsWith("-");
    }

    @Override
    public boolean isEntity(String param) {
        return !param.trim().startsWith("-");
    }

    @Override
    public Character process(String modifier, Object entity) {
        Character character = null;
        assert entity instanceof Character;
        character = (Character) entity;

        if (modifier != null) {
            character.setSaWoInitParams(modifier);
        }
        if (character.alreadyDealt()) {
            return character;
        }
        character.dealInitiativeCards(deck);
        return character;
    }
}
