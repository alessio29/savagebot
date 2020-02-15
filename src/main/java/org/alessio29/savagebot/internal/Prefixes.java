package org.alessio29.savagebot.internal;

import java.util.HashMap;
import java.util.Map;

public class Prefixes {
    public static final String DEFAULT_BOT_PREFIX = "!";
    public static final String DEFAULT_TEST_PREFIX = "~";
    public static String DEFAULT_PREFIX = DEFAULT_BOT_PREFIX;
    private static final String PREFIXES_KEY = "prefixes";
    // userId -> prefix
    private static Map<String, String> prefixes = new HashMap<>();

    public static String getPrefix(String userId) {
        String res = prefixes.get(userId);
        return res == null ? DEFAULT_PREFIX : res;
    }

    public static void setPrefix(String userId, String prefix) {
        prefixes.put(userId, prefix);
        save2Redis();
    }

    private static void save2Redis() {
        RedisClient.saveMapAtKey(PREFIXES_KEY, prefixes);
    }

    public static void loadFromRedis() {
        prefixes = RedisClient.loadMapAtKey(PREFIXES_KEY);
        if (prefixes == null) {
            prefixes = new HashMap<>();
        }
    }

    public static void setDebugPrefix() {
        DEFAULT_PREFIX = DEFAULT_TEST_PREFIX;
    }
}
