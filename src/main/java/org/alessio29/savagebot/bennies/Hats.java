package org.alessio29.savagebot.bennies;

import java.util.HashMap;
import java.util.Map;

public class Hats {

	private static final Map<String, Map<String, Hat>> hats = new HashMap<>();

	public static Hat getHat(String guild, String channel, boolean refill) {

		Map<String, Hat> map = hats.get(guild);
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