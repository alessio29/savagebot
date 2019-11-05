package org.alessio29.savagebot.bennies;

import java.util.HashMap;
import java.util.Map;

public class Pockets {

	private static Map<String, Map<String, Map<String, Pocket>>> pockets = new HashMap<> ();

	public static Pocket getPocket(String guild, String channel, String characterName) {

		if (pockets.get(guild)==null) {
			pockets.put(guild, new HashMap<>());
		}
		Map<String, Pocket> characterPockets = pockets.get(guild).get(channel);
		if (characterPockets == null) {
			characterPockets = new HashMap<>(); 
		}

		Pocket pocket = characterPockets.get(characterName);
		
		if (pocket == null) {
			pocket = new Pocket(characterName, guild, channel);
			characterPockets.put(characterName, pocket);
			pockets.get(guild).put(channel, characterPockets);
		}
		return pockets.get(guild).get(channel).get(characterName);
	}

	public static void resetPockets(String guild, String channel) {

		if (pockets.containsKey(guild) && pockets.get(guild).containsKey(channel)) {
			Map<String, Pocket> map = pockets.get(guild).get(channel);
			map.clear();
			pockets.get(guild).put(channel, map);
		}
		
	}
}
