package org.alessio29.savagebot.internal;

import org.alessio29.savagebot.internal.utils.JsonConverter;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.Collections;
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
    private static Logger log = Logger.getLogger(RedisClient.class);

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
        try {
            getClient().hmset(key, map);
        } catch (Exception e) {
            log.debug("Error while saving map to Redis storage.", e);
        }

    }

    public static void remove(String key, String fieldKey) {
        try {
            getClient().hdel(key, fieldKey);
        } catch (Exception e) {
            log.debug("Error while deleting value from Redis storage.", e);
        }
    }

    public static Map<String, String> loadMapAtKey(String key) {
        try {
            return getClient().hgetAll(key);
        } catch (Exception e) {
            log.debug("Error while loading value from Redis storage.", e);
            return Collections.emptyMap();
        }
    }

    public static String asJson (Object o) {
        return JsonConverter.getInstance().toJson(o);
    }

    public static void setTestMode(boolean b) {
        testMode = b;
    }


}