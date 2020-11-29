package com.izzyacademy.sales.utils;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Service
public class RedisCacheUtil {

    private static final int REDIS_DEFAULT_PORT = 6379;

    private String host;

    private int port;

    private final Jedis client;

    public RedisCacheUtil() {
        final String portValue = System.getenv("REDIS_PORT");

        this.host = System.getenv("REDIS_HOST");
        this.port = Integer.parseInt(portValue);

        this.client = new Jedis(this.host, this.port, 8000);

        this.client.connect();
    }

    public Set<String> getKeys() {

        return this.client.keys("*");
    }

    public RedisCacheUtil set(String key, String value) {
        this.client.set(key, value);
        return this;
    }

    public RedisCacheUtil set(byte[] key, byte[] value) {
        this.client.set(key, value);
        return this;
    }

    public String get(String key) {
        return this.client.get(key);
    }

    public byte[] get(byte[] key) {
        return this.client.get(key);
    }

    public void delete(String key) {
        this.client.del(key);
    }

    public void delete(byte[] key) {
        this.client.del(key);
    }

}
