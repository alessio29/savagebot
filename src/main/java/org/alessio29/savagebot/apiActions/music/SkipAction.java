package org.alessio29.savagebot.apiActions.music;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.music.MusicPlayerCommands;

public class SkipAction implements IBotAction {
    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        try {
            return new MusicPlayerCommands(message).skip(message);
        } catch (Exception e) {
            return new CommandExecutionResult(e.getMessage(), 1);
        }
    }
}
