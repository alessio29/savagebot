package org.alessio29.savagebot.savagebot.bennies;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;


public class Hats {

	private static final Map<Guild, Map<Channel, Hat>> hats = new HashMap<>();

	public static Hat getHat(Guild guild, Channel channel, boolean refill) {

		Map<Channel, Hat> map = hats.get(guild);
		if (map == null) {
			map = new HashMap<>();
			hats.put(guild, map);
		}
		
		Hat hat = map.get(channel);
		if (hat==null || refill) {
			hat = createColoredHat();
		}
		map.put(channel, hat);
		return hat;
	}
	
	private static Hat createColoredHat() {
		return new Hat(); 
	}
	
}