package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public class Prefixes {
    public static final String DEFAULT_PREFIX = "~";
    private static final String PREFIXES_KEY = "prefixes";

    private static Map<String, String> prefixes = new HashMap<>();

    public static String getPrefix(User user) {
        String res = prefixes.get(user);
        return res == null ? DEFAULT_PREFIX : res;
    }

    public static String setPrefix(User user, String prefix) {
        return prefixes.put(user.getId(), prefix);
    }

    public static void savePrefixes() {
        RedisClient.getClient().hmset(PREFIXES_KEY, prefixes);
    }
}
