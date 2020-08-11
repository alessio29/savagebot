package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class NewFightAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        Decks.getDeck(message.getGuildId(), message.getChannelId()).shuffle();
        Characters.resetCharactersInitiative(message.getGuildId(), message.getChannelId());

        Rounds.resetRounds(message.getGuildId(), message.getChannelId());
        return new CommandExecutionResult("Deck is shuffled, initiative tracker reset, starting new fight.\n  ========== Round 1 ========== ", 1);
    }
}
