package com.kirisame.gensokyo.daze.blue.frame.redis.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisClient implements RedisClientInterface {
    @Autowired
    private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public void closeJedis(Jedis jedis) {
        jedis.close();
    }

    @Override
    public String set(String key, String value, Jedis jedis) {
        return jedis.set(key, value);
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time, Jedis jedis) {
        return jedis.set(key, value, nxxx, expx, time);
    }

    @Override
    public String get(String key, Jedis jedis) {
        return jedis.get(key);
    }

    @Override
    public Long hSet(String key, String field, String value, Jedis jedis) {
        return jedis.hset(key, field, value);
    }

    @Override
    public String hGet(String key, String filed, Jedis jedis) {
        return jedis.hget(key, filed);
    }

    @Override
    public Long expire(String key, int seconds, Jedis jedis) {
        return jedis.expire(key, seconds);
    }

    @Override
    public Long del(String key, Jedis jedis) {
        return jedis.del(key);
    }

    @Override
    public Long hDel(String key, String[] fields, Jedis jedis) {
        return jedis.hdel(key, fields);
    }
}
