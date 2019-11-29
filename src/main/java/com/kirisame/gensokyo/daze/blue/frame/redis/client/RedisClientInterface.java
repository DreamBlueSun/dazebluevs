package com.kirisame.gensokyo.daze.blue.frame.redis.client;

import redis.clients.jedis.Jedis;

import java.util.Map;

public interface RedisClientInterface {
    Jedis getJedis();

    void closeJedis(Jedis jedis);

    String set(String key, String value, Jedis jedis);

    String set(String key, String value, String nxxx, String expx, long time, Jedis jedis);

    String get(String key, Jedis jedis);

    Long hSet(String key, String field, String value, Jedis jedis);

    String hGet(String key, String filed, Jedis jedis);

    Map<String, String> hGetAll(String key, Jedis jedis);

    Long expire(String key, int seconds, Jedis jedis);

    Long del(String key, Jedis jedis);

    Long hDel(String key, String[] fields, Jedis jedis);
}
