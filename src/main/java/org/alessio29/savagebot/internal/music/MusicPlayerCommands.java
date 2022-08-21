package org.alessio29.savagebot.internal.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.DiscordResponseBuilder;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MusicPlayerCommands {

    private static final int LOW_VOLUME = 10;

    private MessageReceivedEvent event;

    public MusicPlayerCommands(IMessageReceived message) throws Exception {

        if (!(message.getOriginalEvent() instanceof MessageReceivedEvent)) {
            throw new Exception("Cannot retrieve event from message ");
        }
        this.event = (MessageReceivedEvent) message.getOriginalEvent();
    }


    public CommandExecutionResult join(IMessageReceived message) {

        AudioManager audioManager = event.getGuild().getAudioManager();

        // Check whether audio manager already connected
        if (audioManager.isConnected()) {
            return new CommandExecutionResult("Already joined a channel", 1);
        }

        GuildVoiceState state = event.getMember().getVoiceState();

        if (!state.inAudioChannel()) {
            return new CommandExecutionResult("You should be in a voice channel", 1);
        }

        AudioChannel channel = state.getChannel();
        Member botMember = event.getGuild().getSelfMember();

        if (!botMember.hasPermission(channel, Permission.VOICE_CONNECT)) {
            return new CommandExecutionResult("Bot has no permission to join voice channel", 1);
        }
        audioManager.openAudioConnection(channel);
        return new CommandExecutionResult("Joining voice channel", 1);
    }

    public CommandExecutionResult leave(IMessageReceived message) {

        AudioManager audioManager = event.getGuild().getAudioManager();
        if (!audioManager.isConnected()) {
            return new CommandExecutionResult("Not connected to voice channel", 1);
        }
        AudioChannel channel = audioManager.getConnectedChannel();
        if (!channel.getMembers().contains(event.getMember())) {
            return new CommandExecutionResult("Bot connected to another voice channel", 1);
        }

        audioManager.closeAudioConnection();
        return new CommandExecutionResult("Bot disconnected from voice channel", 1);
    }

    public CommandExecutionResult play(IMessageReceived message, String[] args) {

        if (!isUrlCorrect(args[0])) {
            return new CommandExecutionResult("Provided url must be youtube, soundcloud or bandcamp link", 2);
        }

        PlayerManager manager = PlayerManager.getInstance();

        manager.loadAndPlay(event.getChannel().asTextChannel(), args[0]);
        manager.getGuildMusicManager(event.getGuild()).player.setVolume(LOW_VOLUME);

        return new CommandExecutionResult("Playing track " + args[0], 2);
    }


    private static boolean isUrlCorrect(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public CommandExecutionResult stop(IMessageReceived message) {

        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());
        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);

        return new CommandExecutionResult("Stopping player", 1);


    }

    public CommandExecutionResult queue(IMessageReceived message, String[] args) {

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (queue.isEmpty()) {
            return new CommandExecutionResult("Queue is empty", 1);
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> trackList = new ArrayList<>(queue);

        ReplyBuilder builder = new ReplyBuilder();
        builder.attach("Current queue (Total: " + queue.size() + ")\n");

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = trackList.get(i);
            AudioTrackInfo info = track.getInfo();
            builder.attach(
                    String.format(
                    "%s - %s\n",
                    info.title,
                    info.author
            ));
        }

        return new CommandExecutionResult(builder.toString(), 1);
    }

    public CommandExecutionResult skip(IMessageReceived message) {

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            return new CommandExecutionResult("Player stopped", 1);
        }
        scheduler.nextTrack();
        return new CommandExecutionResult("Playing next track", 1);
    }

    public CommandExecutionResult nowPlaying(IMessageReceived message) {

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;
        if (player.getPlayingTrack() == null) {
            return new CommandExecutionResult("Doesn't playing any track", 1);
        }
        AudioTrackInfo info = player.getPlayingTrack().getInfo();
        ReplyBuilder b = new ReplyBuilder();
        b.attach(ReplyBuilder.bold(info.author)).tab().attach(info.title);
        return new CommandExecutionResult(b.toString(), 1);
    }
}
