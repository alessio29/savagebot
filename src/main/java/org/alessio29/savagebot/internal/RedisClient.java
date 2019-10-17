package org.alessio29.savagebot.internal;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

public class RedisClient {

    private static Jedis client;
    private static String host;
    private static int port;
    private static String pass;
    private static final boolean DEBUG = false;
    private static final Gson converter = new Gson();

    public static Jedis getClient() {
        if (client == null) {
            init(host, port, pass);
        }
        return client;
    }

    public static void init(String redisHost, int redisPort, String redisPass) {
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
        RedisClient.host = redisHost;
        RedisClient.port = redisPort;
        RedisClient.pass = redisPass;
    }

    public static void storeObject(String redisKey, Object data2store) {
        if (data2store == null) {
            return;
        }
        String json = converter.toJson(data2store);
        RedisClient.getClient().set(redisKey, json);
    }

    public static <T> T loadObject(String redisKey, Class<T> clazz ) {
        String json = RedisClient.getClient().get(redisKey);
        if (json == null) {
            return null;
        }
        return converter.fromJson(json, clazz);
    }
}