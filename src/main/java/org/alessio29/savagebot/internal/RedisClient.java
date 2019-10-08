package org.alessio29.savagebot.internal;

import redis.clients.jedis.Jedis;

public class RedisClient {

    private static Jedis client;
    private static String host;
    private static int port;
    private static String pass;

    public static Jedis getClient() {

        if (client == null) {
            init(host, port, pass);
        }

        return client;
    }

    public static void init(String redisHost, int redisPort, String redisPass) {
        client = new Jedis(redisHost, redisPort);
        client.auth(redisPass);
    }

    public static void setup(String redisHost, int redisPort, String redisPass) {
        RedisClient.host = redisHost;
        RedisClient.port = redisPort;
        RedisClient.pass = redisPass;
    }
}
