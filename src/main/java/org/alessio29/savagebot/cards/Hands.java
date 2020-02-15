package org.alessio29.savagebot.cards;

import org.alessio29.savagebot.internal.RedisClient;
import org.alessio29.savagebot.internal.utils.JsonConverter;

import java.util.HashMap;
import java.util.Map;


public class Hands {

    private static final String REDIS_HANDS_KEY = "hands";
    // guildId, channelId, userId -> hand
    private static Map<String, Map<String, Map<String, Hand>>> hands = new HashMap<>();

    public static Hand getHand(String guildId, String channelId, String userId) {

        Map<String, Map<String, Hand>> map = hands.computeIfAbsent(guildId, k -> new HashMap<>());
        Map<String, Hand> map1 = map.get(channelId);
        if (map1 == null) {
            map1 = new HashMap<>();
        }
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

    public static void save2Redis() {

        Map<String, String> map = new HashMap<>();
        if (hands.keySet().isEmpty()) {
            return;
        }
        for (String guildID : hands.keySet()) {
            if (hands.get(guildID) == null || hands.get(guildID).keySet().isEmpty()) {
                continue;
            }
            for (String channelID : hands.get(guildID).keySet()) {
                if (hands.get(guildID).get(channelID) == null || hands.get(guildID).get(channelID).keySet().isEmpty()) {
                    continue;
                }
                for (String userName : hands.get(guildID).get(channelID).keySet()) {
                    Hand h = hands.get(guildID).get(channelID).get(userName);
                    if (h == null) {
                        continue;
                    }
                    String key = guildID + RedisClient.DELIMITER + channelID + RedisClient.DELIMITER + userName;
                    map.put(key, RedisClient.asJson(h));
                }
            }
        }
        RedisClient.saveMapAtKey(REDIS_HANDS_KEY, map);
    }

    public static void loadFromRedis() {
        Map<String, String> map = RedisClient.loadMapAtKey(REDIS_HANDS_KEY);
        for (String key : map.keySet()) {
            String[] keyParts = key.split(RedisClient.DELIMITER);
            if (keyParts.length < 3) {
                // key is wrong
                continue;
            }
            storeHand(keyParts[0], keyParts[1], keyParts[2], JsonConverter.getInstance().fromJson(map.get(key), Hand.class));
        }
    }

    private static void storeHand(String guild, String channel, String user, Hand hand) {
        Map<String, Hand> map = getHands(guild, channel);
        map.put(user, hand);
        save2Redis();
    }
}