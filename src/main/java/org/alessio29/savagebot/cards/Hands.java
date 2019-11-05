package org.alessio29.savagebot.cards;

import org.alessio29.savagebot.internal.RedisClient;

import java.util.HashMap;
import java.util.Map;


public class Hands {

	private static final String REDIS_HANDS_KEY = "hands";

	private static Map<String, Map<String, Hand>> hands = new HashMap<>();

	public static Hand getHand(String guildId, String userId) {

		Map<String, Hand> map = hands.computeIfAbsent(guildId, k -> new HashMap<>());
		Hand hand = map.get(userId);
		if (hand == null) {
			hand = new Hand(userId);
			map.put(userId, hand);
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