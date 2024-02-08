package Caches;

import Util.DataUsage;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import Interfaces.ICache;
import Interfaces.IEvictionStrategy;

public class MemoryCache<K extends Serializable, V extends Serializable> implements ICache<K, V>, Serializable {
    private final Path directory;
    private Map<K, V> cache = new HashMap<>();
    public Map<K, DataUsage> dataUsage = new HashMap<>();

    private int capacity;
    private IEvictionStrategy<K> evictionStrategy;

    public MemoryCache(int capacity, IEvictionStrategy<K> evictionStrategy) throws IOException, ClassNotFoundException {
        this(capacity, evictionStrategy, "Memory");
    }

    public MemoryCache(int capacity, IEvictionStrategy<K> evictionStrategy, String directory) throws IOException, ClassNotFoundException {
        this.capacity = capacity;
        this.evictionStrategy = evictionStrategy;
        this.directory = Paths.get(directory);
        Files.createDirectories(this.directory);
        loadCache();
        loadDataUsage();
    }

    public void putData(K key, V value) throws IOException {
        if (cache.size() >= capacity) {
            evictionStrategy.evictData((ICache<K, ?>) this, dataUsage);
        }
        cache.put(key, value);
        dataUsage.put(key, new DataUsage());
        saveCache();
        saveDataUsage();
    }

    public V getData(K key) throws IOException {
        DataUsage usage = dataUsage.get(key);
        if (usage != null) {
            usage.updateUsage();
            saveDataUsage();
        }
        return cache.get(key);
    }

    // TEST
    public V getDataWithoutUpdate(K key) {
        return cache.get(key);
    }

    private void saveCache() throws IOException {
        Path file = directory.resolve("cache.txt");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
            out.writeObject(cache);
        }
    }

    private void loadDataUsage() throws IOException, ClassNotFoundException {
        Path file = directory.resolve("dataUsage.txt");
        if (Files.exists(file)) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toFile()))) {
                dataUsage = (Map<K, DataUsage>) in.readObject();
            }
        }
    }

    private void saveDataUsage() throws IOException {
        Path file = directory.resolve("dataUsage.txt");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
            out.writeObject(dataUsage);
        }
    }

    private void loadCache() throws IOException, ClassNotFoundException {
        Path file = directory.resolve("cache.txt");
        if (Files.exists(file)) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toFile()))) {
                cache = (Map<K, V>) in.readObject();
            }
        }
    }

    public String[] getKeys() {
        return cache.keySet().stream().map(Object::toString).toArray(String[]::new);
    }

    @Override
    public void removeData(K key) {
        cache.remove(key);
        dataUsage.remove(key);
        // TODO: save cache and dataUsage

    }

    @Override
    public void clearAllData() {
        cache.clear();
        dataUsage.clear();
        // TODO: save cache and dataUsage
    }
}