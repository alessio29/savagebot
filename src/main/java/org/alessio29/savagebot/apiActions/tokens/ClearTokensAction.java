package org.alessio29.savagebot.apiActions.tokens;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.Map;

public class ClearTokensAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length <1) {
            return new CommandExecutionResult("Please provide character name or 'all' to clear all characters", 1);
        }
        Map<String, Character> chars = Characters.getCharacters(message.getGuildId(), message.getChannelId());
        String text;
        if (args[0].trim().toLowerCase().equals("all")) {

            for (Character c : chars.values()) {
                c.removeAllTokens();
            }
            text = "Removed tokens for all characters";
        } else {
            chars.get(args[0]).removeAllTokens();
            text = "Removed tokens for character "+args[0];
        }
//        Characters.storeAllCharacters(message.getGuildId(), message.getChannelId(), chars.values());
        return new CommandExecutionResult(text, args.length+1);
    }
}
