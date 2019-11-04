package org.alessio29.savagebot.apiActions.initiative;

import net.dv8tion.jda.core.entities.Guild;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class NewRoundAction {

    public CommandExecutionResult doAction(String userId, String guildId, String[] args) {

        Rounds.nextRound(guildId, userId);
        Integer round = Rounds.getGuildRound(guildId, userId);
        Deck deck = Decks.getDeck(guildId, userId);
        CharacterInitCache.resetCharactersInitiative(guildId);
        String message = "";
        if (deck.isJokerDealt()) {
            deck.shuffle();
            deck.setJokerDealt(false);
            message = " Joker was dealt in last round, deck is shuffled.\n" ;
        }
        message +=" ========== Round "+round+" ========== ";
        return new CommandExecutionResult(message, 1);
    }
}
