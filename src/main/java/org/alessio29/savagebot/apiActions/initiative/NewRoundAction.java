package org.alessio29.savagebot.apiActions.initiative;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class NewRoundAction implements IDiscordAction {

    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        Guild guild = event.getGuild();
        Rounds.nextRound(guild, event.getTextChannel());
        Integer round = Rounds.getGuildRound(guild, event.getTextChannel());
        Deck deck = Decks.getDeck(guild, event.getChannel());
        CharacterInitCache.resetCharactersInitiative(guild);
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
