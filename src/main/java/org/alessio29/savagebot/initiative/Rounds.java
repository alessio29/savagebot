package org.alessio29.savagebot.initiative;

import java.util.HashMap;
import java.util.Map;


public class Rounds {
	// GuildId, ChannelId, round
	private static Map<String, Map<String, Integer>> rounds = new HashMap<>();
	
	public static Integer getGuildRound(String guild, String channel) {

		Map<String, Integer> map = initGuildRounds(guild);
		Integer round = map.get(channel);
		if(round == null) {
			Rounds.setRound(guild, channel, 1);
		}
		return rounds.get(guild).get(channel);
	}

	private static Map<String, Integer> initGuildRounds(String guild) {
		Map<String, Integer> map = rounds.get(guild);
		if (map == null) {
			map = new HashMap<>(); 
		}
		rounds.put(guild, map);
		return map;
	}
		
	private static void setRound(String guild, String channel, int round) {
		rounds.get(guild).put(channel, round);
	}

	public static void resetRounds(String guild, String channel) {
		Map<String, Integer> round = initGuildRounds(guild);
		round.put(channel, 1);		
	}

	public static void nextRound(String guild, String channel) {
		
		int r = getGuildRound(guild, channel);
		r++;
		rounds.get(guild).put(channel, r);
		
	}
}
