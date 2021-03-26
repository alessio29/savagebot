package org.alessio29.savagebot.internal.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

    public void loadAndPlay (TextChannel channel, String url) {

        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                channel.sendMessage("Adding track "+audioTrack.getInfo().title).queue();
                play(musicManager, audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack first = audioPlaylist.getSelectedTrack();

                if (first == null) {
                    first = audioPlaylist.getTracks().remove(0);
                }

                channel.sendMessage("Adding track "+first.getInfo().title +" from playlist "+audioPlaylist.getName() ).queue();
                play(musicManager, first);
                audioPlaylist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Can't find track "+url).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Can't play track, reason: "+e.getMessage()).queue();
            }
        });
    }

    private void play (GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public synchronized GuildMusicManager getGuildMusicManager (Guild guild) {

        long id = guild.getIdLong();

        GuildMusicManager musicManager = musicManagers.get(id);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(id, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }
}
