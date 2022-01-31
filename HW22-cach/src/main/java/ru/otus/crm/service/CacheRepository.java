package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class CacheRepository<K, V> {
    private static final Logger log = LoggerFactory.getLogger(CacheRepository.class);

    public Map<K, V> cache;
    public HwListener<K, V> listener;
    public List<HwListener<K, V>> listeners;

    public CacheRepository() {
        this.cache = new WeakHashMap<K, V>();
        this.listener = new HwListener<K, V>() {
            @Override
            public void notify(K key, V value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);

            }
        };
        this.listeners = new ArrayList<>();
    }

}
