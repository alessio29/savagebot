package org.alessio29.savagebot.commands;

import org.alessio29.savagebot.apiActions.music.*;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

@CommandCategoryOwner(CommandCategory.MUSIC)
public class MusicCommands {


    @CommandCallback(
            name = "play",
            description = "Plays music",
            aliases = {"pl"},
            arguments = {"music_URL"}
    )
    public static CommandExecutionResult play(IMessageReceived message, String[] args) {
        return new PlayAction().doAction(message, args);
    }

    @CommandCallback(
            name = "join",
            description = "Summons bot to voice channel you are in",
            aliases = {"jn"},
            arguments = {}
    )
    public static CommandExecutionResult join(IMessageReceived message, String[] args) {
        return new JoinAction().doAction(message, args);
    }

    @CommandCallback(
            name = "leave",
            description = "Orders bot to leave voice channel",
            aliases = {"lv"},
            arguments = {}
    )
    public static CommandExecutionResult leave(IMessageReceived message, String[] args) {
        return new LeaveAction().doAction(message, args);
    }

    @CommandCallback(
            name = "stop",
            description = "Orders bot to stop playing",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult stop(IMessageReceived message, String[] args) {
        return new StopAction().doAction(message, args);
    }

    @CommandCallback(
            name = "queue",
            description = "Shows current queue",
            aliases = {"que"},
            arguments = {}
    )
    public static CommandExecutionResult queue(IMessageReceived message, String[] args) {
        return new QueueAction().doAction(message, args);
    }

    @CommandCallback(
            name = "skip",
            description = "Starts playing next track in current in queue",
            aliases = {},
            arguments = {}
    )
    public static CommandExecutionResult skip(IMessageReceived message, String[] args) {
        return new SkipAction().doAction(message, args);
    }

    @CommandCallback(
            name = "nowplaying",
            description = "Shows current track being played",
            aliases = {"np"},
            arguments = {}
    )
    public static CommandExecutionResult nowPlaying(IMessageReceived message, String[] args) {
        return new NowPlayingAction().doAction(message, args);
    }

}
