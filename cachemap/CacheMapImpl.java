import java.util.*;

public class CacheMapImpl<KeyType, ValueType> implements CacheMap<KeyType, ValueType> {
    private long timeToLive;
    private HashMap<KeyType, ValueType> store = new HashMap<>();
    private TreeMap<Long, LinkedList<KeyType>> expirationStorage = new TreeMap<>();

    @Override
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public long getTimeToLive() {
        return timeToLive;
    }

    @Override
    public ValueType put(KeyType key, ValueType value) {
        clearExpired();
        if (key == null || value == null) throw new IllegalArgumentException("Nor key or value can be null");
        addKey(key);
        return store.put(key, value);
    }

    @Override
    public void clearExpired() {
        Set<KeyType> removalSet = new HashSet<>();
        NavigableMap<Long, LinkedList<KeyType>> expiredMap = expirationStorage.subMap(0L, true, Clock.getTime(), true);
        for (List<KeyType> keysList : expiredMap.values()) {
            removalSet.addAll(keysList);
        }
        for (KeyType removeKey : removalSet) {
            store.remove(removeKey);
        }
        for (Long key : expiredMap.keySet()) {
            expirationStorage.remove(key);
        }
    }

    @Override
    public void clear() {
        clearExpired();
        store.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        clearExpired();
        return store.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        clearExpired();
        return store.containsValue(value);
    }

    @Override
    public ValueType get(Object key) {
        clearExpired();
        return store.get(key);
    }

    @Override
    public boolean isEmpty() {
        clearExpired();
        return store.isEmpty();
    }

    @Override
    public ValueType remove(Object key) {
        clearExpired();
        return store.remove(key);
    }

    @Override
    public int size() {
        clearExpired();
        return store.size();
    }

    private void addKey(KeyType key) {
        LinkedList<KeyType> keyList = expirationStorage.get(Clock.getTime() + getTimeToLive());
        if (keyList == null) {
            keyList = new LinkedList<>();
            expirationStorage.put(Clock.getTime() + getTimeToLive(), keyList);
        }
        if (!keyList.contains(key)) keyList.add(key);
    }
}
