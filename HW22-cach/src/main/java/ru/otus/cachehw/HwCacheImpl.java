package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.service.DbServiceClientImpl;


public class HwCacheImpl<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    HwCache<K, V> cache = new HwCacheImpl<>();

    public HwListener<K, V> listener = new HwListener<K, V>() {
        @Override
        public void notify(K key, V value, String action) {
            log.info("key:{}, value:{}, action: {}", key, value, action);

        }
    };


    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }


    @Override
    public void addListener(HwListener<K, V> listener) {
        cache.addListener(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        cache.removeListener(listener);
    }
}
