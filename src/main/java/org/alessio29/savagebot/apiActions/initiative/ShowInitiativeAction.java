package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class ShowInitiativeAction implements IBotAction {

    private static final int CARDS_SIZE = 10;
    private static final int CHAR_NAME_SIZE = 25;
    private static final int TOKENS_SIZE = 5;
    private static final int STATES_SIZE = 35;
    private static final int ALL_CARDS_SIZE = 50;
    private static final int PARAMS_SIZE = 5;

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        ReplyBuilder reply = new ReplyBuilder();
        Integer round = Rounds.getGuildRound(message.getGuildId(), message.getChannelId());
        reply.attach(" ========== Round " + round + " ========== ");
        reply.blockQuote().newLine();
        Set<Character> chars = Characters.getFightingCharactersWithCards(message.getGuildId(), message.getChannelId());
        if (!chars.isEmpty()) {
            List<Character> sortedList = new ArrayList<>(chars);
            sortedList.sort((o1, o2) -> -o1.getBestCard().compareTo(o2.getBestCard()));
            for (Character c : sortedList) {
                String allCards = c.getAllCards().stream().map(Card::toString).collect(Collectors.joining(", "));
                String paramString;
                if (c.getSaWoInitParams().trim().isEmpty()) {
                    paramString = "    ";
                } else {
                    paramString = new ReplyBuilder().
                            squareBracketOpen().
                            rightPad(c.getSaWoInitParams(), 2).
                            squareBracketClose().
                            toString();
                }
                reply.rightPad(c.getName(), CHAR_NAME_SIZE).
                        rightPad(c.getStatesString(), STATES_SIZE).
                        rightPad(" ["+ Utils.notNullValue(c.getTokens())+"]", TOKENS_SIZE).tab().
                        rightPad(paramString, PARAMS_SIZE).tab().
                        rightPad(c.getBestCard().toString(), CARDS_SIZE).tab().
                        rightPad("["+allCards+"]", ALL_CARDS_SIZE).
                        newLine();
            }
        } else {
            reply.attach("No cards dealt!");
        }
        reply.newLine().blockQuote();
        return new CommandExecutionResult(reply.toString(), args.length + 1);

    }
}
