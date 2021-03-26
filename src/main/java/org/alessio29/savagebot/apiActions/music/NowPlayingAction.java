package org.alessio29.savagebot.apiActions.music;

import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.music.MusicPlayerCommands;

public class NowPlayingAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        try {
            return new MusicPlayerCommands(message).nowPlaying(message);
        } catch (Exception e) {
            return new CommandExecutionResult(e.getMessage(), 1);
        }
    }
}
