package org.alessio29.savagebot.internal;

import org.alessio29.savagebot.internal.utils.JsonConverter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.Map;

public class RedisClient {

    private static final String defaultHost = "localhost";
    private static final int defaultPort = 6379;
    private static final int TIMEOUT = 3000;
    private static JedisPool jedisPool;

    private static String host;
    private static int port;
    private static String pass;
    public static final String DELIMITER = ":";
    private static final Logger log = LogManager.getLogger(RedisClient.class);

    private static Jedis getClient() {
        if (jedisPool == null ) {

            GenericObjectPoolConfig jedisConfig = new GenericObjectPoolConfig();
            jedisConfig.setMaxTotal(3);
            jedisConfig.setMaxIdle(3);
            host = (host == null || host.trim().isEmpty()) ? defaultHost : host;
            port = (port == 0) ? defaultPort : port;

            jedisPool = new JedisPool(jedisConfig, host, port, TIMEOUT, pass);

        }
        return jedisPool.getResource();
    }

    public static void setup(String redisHost, int redisPort, String redisPass) {

        RedisClient.host = redisHost;
        RedisClient.port = redisPort;
        RedisClient.pass = redisPass;
    }

    public static void saveMapAtKey(String key, Map map) {
        try (Jedis client = getClient()) {
            client.hmset(key, map);
        } catch (Exception e) {
            log.debug("Error while saving map to Redis storage.", e);
        }
    }

    public static void remove(String key, String fieldKey) {

        try (Jedis client = getClient()) {
            client.hdel(key, fieldKey);
        } catch (Exception e) {
            log.debug("Error while deleting value from Redis storage.", e);
        }
    }

    public static Map<String, String> loadMapAtKey(String key) {
        try (Jedis client = getClient()) {
            return client.hgetAll(key);
        } catch (Exception e) {
            log.debug("Error while loading value from Redis storage.", e);
            return Collections.emptyMap();
        }
    }

    public static String asJson (Object o) {
        return JsonConverter.getInstance().toJson(o);
    }
}