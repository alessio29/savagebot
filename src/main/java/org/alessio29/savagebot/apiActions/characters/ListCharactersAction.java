package org.alessio29.savagebot.apiActions.characters;

import org.alessio29.savagebot.bennies.BennyType;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;
import org.alessio29.savagebot.internal.utils.Utils;

import java.util.Collection;
import java.util.Comparator;

public class ListCharactersAction {

    private static final int NAME_SIZE = 20;
    private static final int TOKEN_SIZE = 10;
    private static final int BENNIES_SIZE = 20;
    private static final int STATES_SIZE = 35;

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        Collection<Character> chars = Characters.getCharacters(message.getGuildId(), message.getChannelId()).values();
        if (chars.isEmpty()) {
            return new CommandExecutionResult("No characters found!", 1);
        }
        BennyType bType = ChannelConfigs.getChannelConfig(message.getChannelId()).getBennyType();

        ReplyBuilder replyBuilder = new ReplyBuilder();
        replyBuilder.blockQuote().
                rightPad("NAME", NAME_SIZE).
                rightPad("TOKENS", TOKEN_SIZE).
                rightPad("BENNIES", BENNIES_SIZE).
                rightPad("STATES", STATES_SIZE).
                newLine();

        chars.stream().sorted(new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }).forEach(chr -> replyBuilder.rightPad(chr.getName(), NAME_SIZE).
                rightPad(String.valueOf(Utils.notNullValue(chr.getTokens())), TOKEN_SIZE).
                rightPad(chr.getBennyValue(bType), BENNIES_SIZE).
                rightPad(Utils.notNullValue(chr.getStatesString()), STATES_SIZE).
                newLine());
        replyBuilder.blockQuote();
        return new CommandExecutionResult(replyBuilder.toString(), 2);
    }
}