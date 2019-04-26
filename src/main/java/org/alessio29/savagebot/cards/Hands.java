package org.alessio29.savagebot.cards;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;


public class Hands {

	private static Map<Guild, Map<User, Hand>> hands = new HashMap<Guild, Map<User,Hand>>();

	public static Hand getHand(Guild guild, User user) {
		
		Map<User, Hand> map = hands.get(guild);
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