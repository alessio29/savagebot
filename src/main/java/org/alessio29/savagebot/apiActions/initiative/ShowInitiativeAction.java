package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.*;
import java.util.stream.Collectors;

public class ShowInitiativeAction {

    private static final int CARDS_SIZE = 8;
    private static final int CHAR_NAME_SIZE = 25;

    public CommandExecutionResult doAction(String guildId, String channelId, String[] args) {

        ReplyBuilder reply = new ReplyBuilder();
        Integer round = Rounds.getGuildRound(guildId, channelId);
        reply.attach(" ========== Round " + round + " ========== ");
        reply.blockQuote().newLine();
        Set<Character> chars = Characters.getFightingCharactersWithCards(guildId, channelId);
        if (!chars.isEmpty()) {
            List<Character> sortedList = new ArrayList<>(chars);
            sortedList.sort((o1, o2) -> -o1.getBestCard().compareTo(o2.getBestCard()));
            for (Character c : sortedList) {
                String allCards = c.getAllCards().stream().map(Card::toString).collect(Collectors.joining(", "));
                String paramString;
                if (c.getParams().trim().isEmpty()) {
                    paramString = "    ";
                } else {
                    paramString = new ReplyBuilder().
                            squareBracketOpen().
                            rightPad(c.getParams(), 2).
                            squareBracketClose().
                            toString();
                }
                reply.rightPad(c.getName(), CHAR_NAME_SIZE).tab().
                        attach(paramString).tab().
                        leftPad(c.getBestCard().toString(), CARDS_SIZE).tab().
                        squareBracketOpen().
                        attach(allCards).
                        squareBracketClose().
                        newLine();
            }
        } else {
            reply.attach("No cards dealt!");
        }
        reply.newLine().blockQuote();
        return new CommandExecutionResult(reply.toString(), args.length + 1);

    }
}
