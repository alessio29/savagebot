package org.alessio29.savagebot.cards;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.alessio29.savagebot.internal.RedisClient;

import java.util.HashMap;
import java.util.Map;


public class Hands {

	private static final String REDIS_HANDS_KEY = "hands";

	private static Map<Guild, Map<User, Hand>> hands = new HashMap<>();

	public static Hand getHand(Guild guild, User user) {

		Map<User, Hand> map = hands.computeIfAbsent(guild, k -> new HashMap<>());
		Hand hand = map.get(user);
		if (hand == null) {
			hand = new Hand(user);
			map.put(user, hand);
		}
		return hand;
	}

	public static void saveHands() {
		RedisClient.storeObject(REDIS_HANDS_KEY, hands);
	}

	public static void loadHands() {
		hands = RedisClient.loadObject(REDIS_HANDS_KEY, HashMap.class);
	}

}