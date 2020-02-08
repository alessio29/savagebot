package org.alessio29.savagebot.apiActions.characters;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.Utils;

import java.util.Collection;

public class ListCharactersAction implements IBotAction {

    private static final int NAME_SIZE = 20;
    private static final int TOKEN_SIZE = 10;
    private static final int STATES_SIZE = 35;

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        Collection<Character> chars = Characters.getCharacters(message.getGuildId(), message.getChannelId()).values();
        if (chars.isEmpty()) {
            return new CommandExecutionResult("No characters found!", 1);
        }
        ReplyBuilder replyBuilder = new ReplyBuilder();
        replyBuilder.blockQuote().
                rightPad("NAME", NAME_SIZE).
                rightPad("TOKENS", TOKEN_SIZE).
                rightPad("STATES", STATES_SIZE).
                newLine();
        for (Character chr : chars) {
            replyBuilder.rightPad(chr.getName(), NAME_SIZE).
                    rightPad(String.valueOf(Utils.notNullValue(chr.getTokens())), TOKEN_SIZE).
                    rightPad(Utils.notNullValue(chr.getStatesString()), STATES_SIZE).
                    newLine();
        }
        replyBuilder.blockQuote();
        return new CommandExecutionResult(replyBuilder.toString(), 2);
    }
}