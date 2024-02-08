package Evictions;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import Interfaces.ICache;
import Interfaces.IEvictionStrategy;
import Util.DataUsage;

public class LeastRecentlyUsed<K> implements IEvictionStrategy<K>, Serializable {

        private final Comparator<DataUsage> comparator = new Comparator<DataUsage>() {
        @Override
        public int compare(DataUsage o1, DataUsage o2) {
            return o1.getLastUsed().compareTo(o2.getLastUsed());
        }
    };

    @Override
    public void evictData(ICache<K, ?> cache, Map<K, DataUsage> dataUsage) {
        K key = dataUsage.entrySet().stream().min(Map.Entry.comparingByValue(comparator)).get().getKey();
        cache.removeData(key);
        dataUsage.remove(key);
    }
}