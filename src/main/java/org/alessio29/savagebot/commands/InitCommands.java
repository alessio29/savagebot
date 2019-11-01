package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.commands.CommandCallback;
import org.alessio29.savagebot.commands.CommandCategory;
import org.alessio29.savagebot.commands.CommandCategoryOwner;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.INITIATIVE)
public class InitCommands {

    @CommandCallback(
            name = "fight",
            description = "Starts new fight: shuffles deck, resets initiative tracker",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult fight(MessageReceivedEvent event, String[] args) {
        Decks.getDeck(event.getGuild(), event.getChannel()).shuffle();
        CharacterInitCache.resetCharactersInitiative(event.getGuild());
        Rounds.resetRounds(event.getGuild(), event.getTextChannel());
        return new CommandExecutionResult("Deck is shuffled, initiative tracker reset, starting new fight.\n  ========== Round 1 ========== ", 1);
    }

    @CommandCallback(
            name = "round",
            description = "Starts new round: resets initiative tracker, shuffles deck, if joker was dealt on previous round",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult round(MessageReceivedEvent event, String[] args) {
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

    @CommandCallback(
            name = "init",
            description = "Shows initiative tracker",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult init(MessageReceivedEvent event, String[] args) {
        return new CommandExecutionResult(ReplyBuilder.showOrder(event), 1);
    }
}
