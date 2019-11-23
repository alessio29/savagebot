package org.alessio29.savagebot.cards;

import org.alessio29.savagebot.internal.RedisClient;

import java.util.HashMap;
import java.util.Map;


public class Hands {

    private static final String REDIS_HANDS_KEY = "hands";
	// guildId, channelId, userId -> hand
    private static Map<String, Map<String, Map<String, Hand>>> hands = new HashMap<>();

    public static Hand getHand(String guildId, String channelId, String userId) {

		Map<String, Map<String, Hand>> map = hands.computeIfAbsent(guildId, k -> new HashMap<>());
		Map<String, Hand> map1 = map.get(channelId);
		Hand hand = null;
		if (map1 != null) {
			hand = map1.get(userId);
		}
        if (hand == null) {
            hand = new Hand(userId);
            map1.put(userId, hand);
            map.put(channelId, map1);
        }
        return hand;
    }

	public static Map<String, Hand> getHands(String guildId, String channelId) {

		Map<String, Map<String, Hand>> map = hands.computeIfAbsent(guildId, k -> new HashMap<>());
		Map<String, Hand> map1 = map.get(channelId);
		if (map1 != null) {
			return map1;
		}
		return new HashMap<>();
	}

    public static void saveHands() {
        RedisClient.storeObject(REDIS_HANDS_KEY, hands);
    }

    public static void loadHands() {
        hands = RedisClient.loadObject(REDIS_HANDS_KEY, HashMap.class);
    }

}