package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class NewFightAction {

    public CommandExecutionResult doAction(String guildId, String channelId, String userId, String[] args) {
        Decks.getDeck(guildId, channelId).shuffle();
        Characters.resetCharactersInitiative(guildId, channelId);

        Rounds.resetRounds(guildId, channelId);
        return new CommandExecutionResult("Deck is shuffled, initiative tracker reset, starting new fight.\n  ========== Round 1 ========== ", 1);
    }
}
