package org.alessio29.savagebot.cards;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class Hands {

	private static Map<IGuild, Map<IUser, Hand>> hands = new HashMap<IGuild, Map<IUser,Hand>>();

	public static Hand getHand(IGuild guild, IUser user) {
		
		Map<IUser, Hand> map = hands.get(guild);
		if (map==null) {
			map = new HashMap<>();
			hands.put(guild, map);
		}
		Hand hand = map.get(user);
		if (hand == null) {
			hand = new Hand(user);
			map.put(user, hand);
		}
		return hand;
	}
}
