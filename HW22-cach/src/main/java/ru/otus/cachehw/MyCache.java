package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    Map<Integer, Integer> cache = new WeakHashMap<>();

    List<WeakReference<String>> list = new ArrayList<>();

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void remove(K key) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {

    }

    @Override
    public void removeListener(HwListener<K, V> listener) {

    }
}
