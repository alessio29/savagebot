package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NewRoundAction {

    public CommandExecutionResult doAction(String guildId, String channelId, String[] args) {

        Rounds.nextRound(guildId, channelId);
        Integer round = Rounds.getGuildRound(guildId, channelId);
        Deck deck = Decks.getDeck(guildId, channelId);
        Characters.clearCharactersCards(guildId, channelId);
        String message = "";
        if (deck.isJokerDealt()) {
            deck.shuffle();
            deck.setJokerDealt(false);
            message = " Joker was dealt in last round, deck is shuffled.\n";
        }


        if (args.length == 0) {
            return new CommandExecutionResult(message, 1);
        }

        boolean reDeal = false;
        List<String> characters2Remove = new ArrayList<>();

        for (String arg : args) {
            if (arg.trim().equals("+")) {
                reDeal = true;
            }
            if (arg.startsWith("-")) {
                String charName = arg.substring(1);
                if (Characters.getCharacterByName(guildId, channelId, charName) != null)
                characters2Remove.add(charName);
            }
        }

        for (String charName : characters2Remove) {
            Character ch = Characters.getCharacterByName(guildId, channelId, charName);
            ch.setOutOfFight(true);
            Characters.storeCharacter(guildId, channelId, ch);
        }


        if (reDeal) {
            // deal cards again according to parameters
            StringBuilder newArgs = new StringBuilder();
            Set<Character> chars = Characters.getFightingCharacters(guildId, channelId);
            for (Character c : chars) {
                newArgs.append(c.getName()).append(ReplyBuilder.SPACE);
                if (!c.getParams().isEmpty()) {
                    newArgs.append("-").append(c.getParams()).append(ReplyBuilder.SPACE);
                }
            }
            String[] str = newArgs.toString().trim().split(ReplyBuilder.SPACE);
            new DealInitiativeCardsAction().doAction(guildId, channelId,  str);
            CommandExecutionResult res = new ShowInitiativeAction().doAction(guildId, channelId, str);
            message += res.getResult();
        } else {
            message += " ========== Round " + round + " ========== ";
        }
        return new CommandExecutionResult(message, args.length+1);
    }
}
