package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.Rounds;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.Utils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShowInitiativeAction {

    private static final int MIN_CARDS_SIZE = 6;
    private static final int MIN_CHAR_NAME_SIZE = 15;
    private static final int TOKENS_SIZE = 8;
    private static final int BENNIES_SIZE = 8;
    private static final int MIN_STATES_SIZE = 8;
    private static final int ALL_CARDS_SIZE = 30;
    private static final int EDGES_SIZE = 6;
    private static final int HOLD_SIZE = 6;

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        ReplyBuilder reply = new ReplyBuilder();
        ReplyBuilder header = new ReplyBuilder();
        header.blockQuote();
        Integer round = Rounds.getGuildRound(message.getGuildId(), message.getChannelId());
        Set<Character> chars = Characters.getFightingCharactersWithCards(message.getGuildId(), message.getChannelId());
        int statesStize;
        int cardsSize;
        int charNameSize;

        if (!chars.isEmpty()) {
            List<Character> sortedList = new ArrayList<>(chars);
            sortedList.sort((o1, o2) -> {
                int r = -Boolean.compare(o1.isOnHold(), o2.isOnHold());
                if (r == 0) {
                    return -o1.getBestCard().compareTo(o2.getBestCard());
                }
                return r;
                });
            charNameSize = sortedList.stream().
                    map(character -> {
                        return character.getName().trim().length() + character.getSaWoInitParams().length() + 2;
                    }).max((Comparator.naturalOrder())).get();
            cardsSize = sortedList.stream().
                    map(character -> {
                        return character.getBestCard().toString().length();
                    }).max((Comparator.naturalOrder())).get();
            statesStize = sortedList.stream().
                    map(character -> {
                        return character.getStatesString().length();
                    }).max((Comparator.naturalOrder())).get();

            charNameSize = Math.max(charNameSize + 2, MIN_CHAR_NAME_SIZE + EDGES_SIZE);
            cardsSize = Math.max(cardsSize + 2, MIN_CARDS_SIZE);
            statesStize = Math.max(statesStize + 2, MIN_STATES_SIZE);

            for (Character c : sortedList) {
                String allCards = c.getInitCards().stream().map(Card::toString).collect(Collectors.joining(", "));

                String holdStatus = StringUtils.rightPad(c.isOnHold()==true?"<H>":"", HOLD_SIZE);

                String edgesString = StringUtils.rightPad(
                        new ReplyBuilder().
                                addSquareBrackets(
                                        Utils.notNullValue(c.getSaWoInitParams())).toString()
                        , EDGES_SIZE);
                String tokensString = StringUtils.rightPad(
                        new ReplyBuilder().
                                addSquareBrackets(
                                        Utils.notNullValue(c.getTokens()).toString()
                                ).toString()
                        , TOKENS_SIZE);

                String benniesString = StringUtils.rightPad(
                        new ReplyBuilder().
                                addSquareBrackets(
                                        Utils.notNullValue(c.getBennies()).toString()
                                ).toString()
                        , BENNIES_SIZE);

                reply.rightPad(c.getName() + holdStatus + " " + edgesString, charNameSize).
                        rightPad(tokensString, TOKENS_SIZE).
                        rightPad(benniesString, BENNIES_SIZE).
                        rightPad(c.getStatesString(), statesStize).
                        rightPad(c.getBestCard().toString(), cardsSize).
                        rightPad("[" + allCards + "]", ALL_CARDS_SIZE).
                        newLine();
            }
            header.attach(" ========== Round " + round + " ========== ");
            header.newLine();
            header.rightPad("NAME [MODS]", charNameSize).
                    rightPad("TOKENS", TOKENS_SIZE).
                    rightPad("BENNIES", BENNIES_SIZE).
                    rightPad("STATES", statesStize).
                    rightPad("CARD", cardsSize).
                    rightPad("ALL CARDS", ALL_CARDS_SIZE).
                    newLine();
        } else {
            reply.attach("No cards dealt!");
        }
        reply.newLine().blockQuote();
        return new CommandExecutionResult(header.toString() + reply.toString(), args.length + 1);
    }
}
