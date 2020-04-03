package org.alessio29.savagebot.internal;

import org.alessio29.savagebot.internal.utils.JsonConverter;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisClient {

    private static final boolean DEBUG = false;
    private static final String defaultHost = "localhost";
    private static final int defaultPort = 6379;
    private static Jedis client;
    private static String host;
    private static int port;
    private static String pass;
    private static boolean testMode = false;
    public static final String DELIMITER = ":";

    private static Jedis getClient() {
        if (client == null) {
            init(host, port, pass);
        }
        return client;
    }

    private static void init(String redisHost, int redisPort, String redisPass) {
        if (testMode) {
            return;
        }

        redisHost = (redisHost == null || redisHost.trim().isEmpty()) ? defaultHost : redisHost;
        redisPort = (redisPort == 0) ? defaultPort : redisPort;
        client = new Jedis(redisHost, redisPort);
        if (DEBUG) {
            System.out.println("Redis connection established.");
        }
        if (redisPass != null) {
            client.auth(redisPass);
            if (DEBUG) {
                System.out.println("Redis client authorized.");
            }
        }
    }

    public static void setup(String redisHost, int redisPort, String redisPass) {
        if (testMode) {
            return;
        }
        RedisClient.host = redisHost;
        RedisClient.port = redisPort;
        RedisClient.pass = redisPass;
    }

    public static void saveMapAtKey(String key, Map map) {
        getClient().hmset(key, map);
    }

    public static void remove(String key, String fieldKey) {
         getClient().hdel(key, fieldKey);
    }

    public static Map<String, String> loadMapAtKey(String key) {
        return getClient().hgetAll(key);
    }

    public static String asJson (Object o) {
        return JsonConverter.getInstance().toJson(o);
    }

    public static void setTestMode(boolean b) {
        testMode = b;
    }


}