package org.alessio29.savagebot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.characters.CharacterInitiative;
import org.alessio29.savagebot.exceptions.CardAlreadyDealtException;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandCategoryOwner(CommandCategory.INITIATIVE)
public class InitCommands {

    private static final String QUICK_MAKER = "q";
    private static final CharSequence HESITANT_MARKER = "h";
    private static final Pattern modPattern = Pattern.compile("^[^ilqh]$");

    @CommandCallback(
            name = "fight",
            description = "Starts new fight: shuffles deck, resets initiative tracker",
            aliases = {"f"},
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
            aliases = {"rd"},
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

    @CommandCallback(
            name = "di",
            description = "Deal initiative card",
            aliases = {},
            arguments = { "<character_name1> [<modifiers_1>] ... <character_nameN> [<modifiers_N>]" }
    )
    public static CommandExecutionResult dealInitCards(MessageReceivedEvent event, String[] args) {

        Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());

        if(deck.isShuffleNeeded()) {
            return new CommandExecutionResult("Shuffle is needed..", args.length+1);
        }

        if (args.length < 1) {
            return new CommandExecutionResult("Provide at least one character name!", 1);
        }

        int index = 0;
        while (index < args.length) {
            String charName = args[index];
            String mods = "";
            if (index+1<args.length && args[index+1].startsWith("-")) {
                // this is initiative modifiers
                index++;
                mods = args[index].trim().toLowerCase().substring(1);
                Matcher m = modPattern.matcher(mods);
                if (m.matches()) {
                    return new CommandExecutionResult(
                            "Mod parameter must be a combination of letters: " +
                                    "h - for Hesitant hindrance, " +
                                    "q - for Quick edge, " +
                                    "l - for Levelheaded edge and" +
                                    " i for Improved Levelheaded edge!", args.length+1);
                }
                if (mods.contains(QUICK_MAKER) && mods.contains(HESITANT_MARKER)) {
                    return new CommandExecutionResult("Impossible modifiers combination: " +
                            "character cannot be quick and hesitant at the same time!", args.length+1);
                }
            }
            if (CharacterInitCache.alreadyDealt(event.getGuild(), charName)) {
                continue;
            }
            DrawCardResult cards = deck.getCardByParams(mods);
            if(cards == null) {
                return new CommandExecutionResult("Deck is empty!", args.length+1);
            }
            try {
                CharacterInitCache.addCharacter(event.getGuild(), new CharacterInitiative(charName, mods, cards));
//				Characters.saveCharacters();
            } catch (CardAlreadyDealtException e) {
                return new CommandExecutionResult("Card already dealt!", args.length+1);
            }
            index++;
         }
        return new CommandExecutionResult(ReplyBuilder.showOrder(event), args.length+1);
    }


}
