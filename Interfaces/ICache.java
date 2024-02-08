package Interfaces;

import java.io.IOException;

public interface ICache<Key, Value> {
    void putData(Key key, Value value) throws IOException;
    Value getData(Key key) throws IOException;
    void removeData(Key key);
    void clearAllData();
}
