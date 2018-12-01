package org.alessio29.savagebot.bennies;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;

public class Hats {

	private static final Map<IGuild, Hat> hats = new HashMap<>();

	public static Hat getHat(IGuild guild) {

		Hat hat = hats.get(guild);
		if (hat == null) {
			Hats.createColoredHat(guild);	
		}
		return hats.get(guild);
	}
	
	private static void createColoredHat(IGuild guild) {
		Hat hat = new Hat(); 
		hats.put(guild, hat);
	}
}
