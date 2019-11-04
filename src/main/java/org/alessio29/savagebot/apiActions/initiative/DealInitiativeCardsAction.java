package org.alessio29.savagebot.apiActions.initiative;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.CharacterInitCache;
import org.alessio29.savagebot.characters.CharacterInitiative;
import org.alessio29.savagebot.exceptions.CardAlreadyDealtException;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DealInitiativeCardsAction implements IDiscordAction {

    private static final String QUICK_MAKER = "q";
    private static final CharSequence HESITANT_MARKER = "h";
    private static final Pattern modPattern = Pattern.compile("^[^ilqh]$");

    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {

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
        return null;
    }
}
