package org.alessio29.savagebot.initiative;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;


public class Rounds {

	private static Map<Guild, Map<Channel, Integer>> rounds = new HashMap<Guild, Map<Channel, Integer>>();
	
	public static Integer getGuildRound(Guild guild, Channel channel) { 

		Map<Channel, Integer> map = initGuildRounds(guild);
		Integer round = map.get(channel);
		if(round == null) {
			Rounds.setRound(guild, channel, 1);
		}
		return rounds.get(guild).get(channel);
	}

	private static Map<Channel, Integer> initGuildRounds(Guild guild) {
		Map<Channel, Integer> map = rounds.get(guild);
		if (map == null) {
			map = new HashMap<>(); 
		}
		rounds.put(guild, map);
		return map;
	}
		
	private static void setRound(Guild guild, Channel channel, int i) {
		
		rounds.get(guild).put(channel, i);
	}

	public static void resetRounds(Guild guild, Channel channel) {
		Map<Channel, Integer> round = initGuildRounds(guild);
		round.put(channel, 1);		
	}

	public static void nextRound(Guild guild, Channel channel) {
		
		int r = getGuildRound(guild, channel);
		r++;
		rounds.get(guild).put(channel, r);
		
	}
}
