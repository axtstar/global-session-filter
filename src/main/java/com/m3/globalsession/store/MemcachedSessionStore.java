package com.m3.globalsession.store;

import com.m3.globalsession.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MemcachedSessionStore implements SessionStore {

    private static final Logger log = LoggerFactory.getLogger(MemcachedSessionStore.class);

    private final MemcachedClient client;

    public MemcachedSessionStore(MemcachedClient client) {
        this.client = client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends Serializable> V get(String key) {
        try {
            V value = (V) client.get(key);
            if (log.isDebugEnabled()) {
                log.debug("Get: " + key + " -> " + value);
            }
            return value;

        } catch (Exception e) {
            log.debug("Failed to get value for " + key, e);
            return null;
        }
    }

    @Override
    public <V extends Serializable> void set(String key, int expire, V value) {

        if (log.isDebugEnabled()) {
            log.debug("Set (expire:" + expire + "): " + key + " -> " + value);
        }

        try {
            if (value == null) {
                client.delete(key);
            } else {
                client.set(key, expire, value);
            }
        } catch (Exception e) {
            log.debug("Failed to set value for " + key, e);
        }
    }

    @Override
    public void remove(String key) {

        if (log.isDebugEnabled()) {
            log.debug("Delete: " + key);
        }

        try {
            client.delete(key);
        } catch (Exception e) {
            log.debug("Failed to delete value for " + key, e);
        }
    }

}