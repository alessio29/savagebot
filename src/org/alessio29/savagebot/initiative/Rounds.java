package org.alessio29.savagebot.initiative;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class Rounds {

	private static Map<IGuild, Map<IChannel, Integer>> rounds = new HashMap<IGuild, Map<IChannel, Integer>>();
	
	public static Integer getGuildRound(IGuild guild, IChannel channel) { 

		Map<IChannel, Integer> map = initGuildRounds(guild);
		Integer round = map.get(channel);
		if(round == null) {
			Rounds.setRound(guild, channel, 1);
		}
		return rounds.get(guild).get(channel);
	}

	private static Map<IChannel, Integer> initGuildRounds(IGuild guild) {
		Map<IChannel, Integer> map = rounds.get(guild);
		if (map == null) {
			map = new HashMap<>(); 
		}
		rounds.put(guild, map);
		return map;
	}
		
	private static void setRound(IGuild guild, IChannel channel, int i) {
		
		rounds.get(guild).put(channel, i);
	}

	public static void resetRounds(IGuild guild, IChannel channel) {
		Map<IChannel, Integer> round = initGuildRounds(guild);
		round.put(channel, 1);		
	}

	public static void nextRound(IGuild guild, IChannel channel) {
		
		int r = getGuildRound(guild, channel);
		r++;
		rounds.get(guild).put(channel, r);
		
	}
}
