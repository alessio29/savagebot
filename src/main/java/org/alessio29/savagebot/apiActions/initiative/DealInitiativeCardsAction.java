package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.DrawCardResult;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DealInitiativeCardsAction implements IBotAction {

    private static final String QUICK_MAKER = "q";
    private static final CharSequence HESITANT_MARKER = "h";
    private static final CharSequence LEVELHEADED_MARKER = "l";
    private static final CharSequence IMPROVEDLEVELHEADED_MARKER = "i";
    private static final Pattern modPattern = Pattern.compile("^[^ilqh]$");



    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        Deck deck = Decks.getDeck(message.getGuildId(), message.getChannelId());

        if (deck.isShuffleNeeded()) {
            return new CommandExecutionResult("Shuffle is needed..", args.length + 1);
        }

        if (args.length < 1) {
            return new CommandExecutionResult("Provide at least one character name!", 1);
        }

        int index = 0;
        while (index < args.length) {
            String charName = args[index];
            String mods = "";
            if (index + 1 < args.length && args[index + 1].startsWith("-")) {
                // this is initiative modifiers
                index++;
                mods = args[index].trim().toLowerCase().substring(1);
                Matcher m = modPattern.matcher(mods);
                if (m.matches()) {
                    return new CommandExecutionResult(
                            "Mod parameter must be a combination of letters: " +
                                    "h - for Hesitant hindrance, " +
                                    "q - for Quick edge, " +
                                    "l - for Levelheaded edge and " +
                                    "i for Improved Levelheaded edge!", args.length + 1);
                }
                if (mods.contains(QUICK_MAKER) && mods.contains(HESITANT_MARKER)) {
                    return new CommandExecutionResult("Impossible modifiers combination: " +
                            "character cannot be quick and hesitant at the same time!", args.length + 1);
                }
                if (mods.contains(LEVELHEADED_MARKER) && mods.contains(HESITANT_MARKER)) {
                    return new CommandExecutionResult("Impossible modifiers combination: " +
                            "character cannot be levelheaded and hesitant at the same time!", args.length + 1);
                }
                if (mods.contains(IMPROVEDLEVELHEADED_MARKER) && mods.contains(HESITANT_MARKER)) {
                    return new CommandExecutionResult("Impossible modifiers combination: " +
                            "character cannot be improved levelheaded and hesitant at the same time!", args.length + 1);
                }
            }
            Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName);
            if (character == null) {
                character = new Character(charName, mods);
            } else {
                mods = character.getParams();
            }
            if ( character.alreadyDealt() || character.isOutOfFight()) {
                index++;
                continue;
            }
            DrawCardResult cards = deck.getCardByParams(mods);
            if (cards == null) {
                return new CommandExecutionResult("Deck is empty!", args.length + 1);
            }
            character.setAllCards(cards.getCards());
            character.setOutOfFight(false);
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
//				Characters.saveCharacters();
            index++;
        }
        return null;
    }
}
