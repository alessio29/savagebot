package org.alessio29.savagebot.bennies;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class Hats {

	private static final Map<IGuild, Map<IChannel, Hat>> hats = new HashMap<>();

	public static Hat getHat(IGuild guild, IChannel channel) {

		Map<IChannel, Hat> map = hats.get(guild);
		if (map == null) {
			map = new HashMap<>();
			hats.put(guild, map);
		}
		
		Hat hat = map.get(channel);
		if (hat == null) {
			Hats.createColoredHat(guild, channel);	
		}
		return hat;
	}
	
	private static void createColoredHat(IGuild guild, IChannel channel) {
		Hat hat = new Hat(); 
		hats.get(guild).put(channel, hat);
	}
}
