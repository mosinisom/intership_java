package Interfaces;

import java.io.Serializable;
import java.util.Map;

import Util.DataUsage;

public interface IEvictionStrategy<K> extends Serializable {
    void evictData(ICache<K, ?> memoryCache, Map<K, DataUsage> dataUsage);
    
}
