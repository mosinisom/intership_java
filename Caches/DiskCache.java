package Caches;

import java.util.Map;
import java.util.Set;

import Interfaces.ICache;
import Interfaces.IEvictionStrategy;
import Util.DataUsage;

public class DiskCache<K, V> implements ICache<K, V> {

    private Map<K, V> cache;
    public Map<K, DataUsage> dataUsage;
    private IEvictionStrategy<K> evictionStrategy;

    private int capacity;

    public DiskCache(int capacity, IEvictionStrategy<K> evictionStrategy) {
        this.capacity = capacity;
        this.cache = new java.util.HashMap<K, V>(capacity);
        this.dataUsage = new java.util.TreeMap<K, DataUsage>();
        this.evictionStrategy = evictionStrategy;
    }

    @Override
    public void putData(K key, V value) {
        if (cache.size() >= capacity) {
            evictionStrategy.evictData(this, dataUsage);
        }
        cache.put(key, value);
        dataUsage.put(key, new DataUsage());
    }

    @Override
    public V getData(K key) {
        dataUsage.get(key).updateUsage();
        return cache.get(key);
    }

    // TEST
    public V getDataWithoutUpdate(K key) {
        return cache.get(key);
    }

    @Override
    public void removeData(K key) {
        cache.remove(key);
        dataUsage.remove(key);
    }

    @Override
    public void clearAllData() {
        cache.clear();
        dataUsage.clear();
    }

    public Set<String> getKeys() {
        return (Set<String>) cache.keySet();
    }

}