package com.izzyacademy.sales.utils;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Set;

@Service
public class RedisCacheUtil {

    private static final int REDIS_DEFAULT_PORT = 6379;

    private String host;

    private int port;

    private int expiration;

    private int timeout;

    private final SetParams setParams;

    private final Jedis client;

    public RedisCacheUtil() {

        final String timeoutSeconds = System.getenv("REDIS_TIMEOUT").trim();

        final String expirationSeconds = System.getenv("REDIS_EXPIRATION").trim();

        final String portValue = System.getenv("REDIS_PORT").trim();

        this.host = System.getenv("REDIS_HOST").trim();
        this.port = Integer.parseInt(portValue);

        this.expiration = Integer.parseInt(expirationSeconds);

        this.timeout = Integer.parseInt(timeoutSeconds) * 1000;

        this.setParams = new SetParams();

        this.setParams.ex(this.expiration);

        this.client = new Jedis(this.host, this.port, this.timeout);

        this.client.connect();
    }

    public Set<String> getKeys() {

        return this.client.keys("*");
    }

    public RedisCacheUtil set(String key, String value) {
        this.client.set(key, value, this.setParams);
        return this;
    }

    public RedisCacheUtil set(byte[] key, byte[] value) {
        this.client.set(key, value, this.setParams);
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
