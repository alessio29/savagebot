package org.alessio29.savagebot.internal.utils;

import org.alessio29.savagebot.bennies.BennyType;
import org.alessio29.savagebot.internal.RedisClient;

import java.util.HashMap;
import java.util.Map;

public class ChannelConfigs {

    private static final String REDIS_CONFIGS_KEY = "configs";
    private static Map<String, ChannelConfig> configs = readConfigs();

    public static ChannelConfig getChannelConfig(String channelId) {
        return configs.computeIfAbsent(channelId, s -> new ChannelConfig(BennyType.NORMAL));
    }

    private static Map<String, ChannelConfig> readConfigs() {
        Map<String, String> map = RedisClient.loadMapAtKey(REDIS_CONFIGS_KEY);
        Map<String, ChannelConfig> result = new HashMap<>();
        for (String key : map.keySet()) {
            result.put(key, JsonConverter.getInstance().fromJson(map.get(key), ChannelConfig.class));
        }
        return result;
    }

    public static void save() {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, ChannelConfig> e : configs.entrySet()) {
            map.put(e.getKey(), RedisClient.asJson(e.getValue()));
        }
        RedisClient.saveMapAtKey(REDIS_CONFIGS_KEY, map);
    }
}
