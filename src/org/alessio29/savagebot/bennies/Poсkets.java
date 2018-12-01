package org.alessio29.savagebot.bennies;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Poñkets {

	private static Map<IGuild, Map<IUser, Pocket>> pockets = new HashMap<> ();
	
	public static Pocket getPocket(IGuild guild, IUser user) {
		
		if (pockets.get(guild)==null) {
			pockets.put(guild, new HashMap<IUser, Pocket>());
		}
		Pocket pocket = pockets.get(guild).get(user);
		if (pocket == null) {
			pockets.get(guild).put(user, new Pocket(user, guild));
		}
		return pockets.get(guild).get(user);
	}

	
}
