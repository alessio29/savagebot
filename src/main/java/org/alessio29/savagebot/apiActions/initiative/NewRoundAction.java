package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NewRoundAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        Rounds.nextRound(message.getGuildId(), message.getChannelId());
        Rounds.getGuildRound(message.getGuildId(), message.getChannelId());
        Deck deck = Decks.getDeck(message.getGuildId(), message.getChannelId());
        Characters.clearCharactersCards(message.getGuildId(), message.getChannelId());
        String txtMessage = "";
        if (deck.isJokerDealt()) {
            deck.shuffle();
            deck.setJokerDealt(false);
            txtMessage = " Joker was dealt in last round, deck is shuffled.\n";
        }

        if (args.length == 0) {
            Integer round = Rounds.getGuildRound(message.getGuildId(), message.getChannelId());
            txtMessage += " ========== Round " + round + " ========== ";
            return new CommandExecutionResult( txtMessage, 1);
        }
        boolean reDeal = false;
        List<String> characters2Remove = new ArrayList<>();
        for (String arg : args) {
            if (arg.trim().equals("+")) {
                reDeal = true;
            }
            if (arg.startsWith("-")) {
                String charName = arg.substring(1);
                if (Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName) != null)
                characters2Remove.add(charName);
            }
        }
        for (String charName : characters2Remove) {
            Character ch = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName);
            ch.setOutOfFight(true);
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), ch);
        }
        if (reDeal) {
            // deal cards again according to parameters
            StringBuilder newArgs = new StringBuilder();
            Set<Character> chars = Characters.getFightingCharacters(message.getGuildId(), message.getChannelId());
            for (Character c : chars) {
                newArgs.append(c.getName()).append(ReplyBuilder.SPACE);
                if (!c.getParams().isEmpty()) {
                    newArgs.append("-").append(c.getParams()).append(ReplyBuilder.SPACE);
                }
            }
            String[] str = newArgs.toString().trim().split(ReplyBuilder.SPACE);
            new DealInitiativeCardsAction().doAction(message, str);
            CommandExecutionResult res = new ShowInitiativeAction().doAction(message, str);
            txtMessage += res.getResult();
        }
        return new CommandExecutionResult(txtMessage, args.length+1);
    }
}
