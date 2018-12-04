package org.alessio29.savagebot.bennies;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class Hats {

	private static final Map<IGuild, Map<IChannel, Hat>> hats = new HashMap<>();

	public static Hat getHat(IGuild guild, IChannel channel, boolean refill) {

		Map<IChannel, Hat> map = hats.get(guild);
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