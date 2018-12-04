package org.alessio29.savagebot.bennies;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;

public class Pockets {

	private static Map<IGuild, Map<String, Pocket>> pockets = new HashMap<> ();
	
	public static Pocket getPocket(IGuild guild, String characterName) {
		
		if (pockets.get(guild)==null) {
			pockets.put(guild, new HashMap<String, Pocket>());
		}
		Pocket pocket = pockets.get(guild).get(characterName);
		if (pocket == null) {
			pockets.get(guild).put(characterName, new Pocket(characterName, guild));
		}
		return pockets.get(guild).get(characterName);
	}

	
}
