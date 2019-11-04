package org.alessio29.savagebot.apiActions.initiative;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class NewFightAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        Decks.getDeck(event.getGuild(), event.getChannel()).shuffle();
        CharacterInitCache.resetCharactersInitiative(event.getGuild());
        Rounds.resetRounds(event.getGuild(), event.getTextChannel());
        return new CommandExecutionResult("Deck is shuffled, initiative tracker reset, starting new fight.\n  ========== Round 1 ========== ", 1);
    }
}
