package org.alessio29.savagebot.internal;

import redis.clients.jedis.Jedis;

public class RedisClient {

    private static Jedis client;

    public static Jedis getClient() {
        return client;
    }

    public static void init(String redisHost, int redisPort, String redisPass) {
        client = new Jedis(redisHost, redisPort);
        client.auth(redisPass);
    }
}
