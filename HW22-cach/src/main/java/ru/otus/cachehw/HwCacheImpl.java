package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.service.CacheRepository;

public class HwCacheImpl<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(HwCacheImpl.class);

    public CacheRepository<K, V> cacheRepository;

    public HwCacheImpl() {
        this.cacheRepository = new CacheRepository<>();
    }



    @Override
    public void put(K key, V value) {
        cacheRepository.listeners.forEach(listener -> listener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        cacheRepository.listeners.forEach(listener -> listener.notify(key, null, "remove"));
    }

    @Override
    public V get(K key) {
        return cacheRepository.cache.get(key);
    }


    @Override
    public void addListener(HwListener<K, V> listener) {
        cacheRepository.listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        cacheRepository.listeners.remove(listener);
    }
}
