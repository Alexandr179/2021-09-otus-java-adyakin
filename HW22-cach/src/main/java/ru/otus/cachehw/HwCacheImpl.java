package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class HwCacheImpl<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(HwCacheImpl.class);

    public Map<K, V> cacheMap = new WeakHashMap<>();
    public List<HwListener<K, V>> listeners = new ArrayList<>();
    public HwListener<K, V> listener = new HwListener<K, V>() {
        @Override
        public void notify(K key, V value, String action) {
            log.info("key:{}, value:{}, action: {}", key, value, action);
            switch (action){
                case "put":
                    cacheMap.put(key, value);
                    break;
                case "remove":
                    cacheMap.remove(key);
                    break;
            }
        }
    };

    public HwCacheImpl() {
        this.listeners.add(listener);
    }

    @Override
    public void put(K key, V value) {
        listeners.forEach(listener -> listener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        listeners.forEach(listener -> listener.notify(key, null, "remove"));
    }

    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }



    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
