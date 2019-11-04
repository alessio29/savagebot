package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class NewFightAction {

    public CommandExecutionResult doAction(String userId, String guildId, String channelId,  String[] args) {
        Decks.getDeck(guildId, channelId).shuffle();
        CharacterInitCache.resetCharactersInitiative(guildId);
        Rounds.resetRounds(guildId, channelId);
        return new CommandExecutionResult("Deck is shuffled, initiative tracker reset, starting new fight.\n  ========== Round 1 ========== ", 1);
    }
}
