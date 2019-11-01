package org.alessio29.savagebot.internal;

import net.dv8tion.jda.core.entities.User;
import org.alessio29.savagebot.internal.RedisClient;

import java.util.HashMap;

public class Prefixes {
    public static final String DEFAULT_PREFIX = "~";
    private static final String PREFIXES_KEY = "prefixes";

    private static HashMap<String, String> prefixes = new HashMap<>();

    public static String getPrefix(User user) {
        String res = prefixes.get(user.getId());
        return res == null ? DEFAULT_PREFIX : res;
    }

    public static void setPrefix(User user, String prefix) {
        prefixes.put(user.getId(), prefix);
        savePrefixes();
    }

    private static void savePrefixes() {
        RedisClient.storeObject(PREFIXES_KEY, prefixes);
    }

    public static void loadPrefixes() {
        prefixes = RedisClient.loadObject(PREFIXES_KEY, HashMap.class);
        if (prefixes == null) {
            prefixes = new HashMap<>();
        }

    }
}
