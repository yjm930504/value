package yjm.value.time;

import java.util.*;

/**
 * 存储历史数据
 */
public class Series<K, V> implements NavigableMap<K, V> {

    private final NavigableMap<K, V> delegate;

    private final Class<K> classK;
    private final Class<V> classV;

    public Series(final Class<K> classK, final Class<V> classV) {
        this.classK = classK;
        this.classV = classV;
        this.delegate = new TreeMap<K, V>();
    }

    @Override
    public Entry<K, V> ceilingEntry(final K key) {
        return delegate.ceilingEntry(key);
    }

    @Override
    public K ceilingKey(final K key) {
        return delegate.ceilingKey(key);
    }

    @Override
    public void clear() {
        delegate.clear();
    }
    @Override
    public Comparator<? super K> comparator() {
        return delegate.comparator();
    }

    @Override
    public boolean containsKey(final Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        return delegate.descendingKeySet();
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        return delegate.descendingMap();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public boolean equals(final Object o) {
        return delegate.equals(o);
    }

    @Override
    public Entry<K, V> firstEntry() {
        return delegate.firstEntry();
    }

    @Override
    public K firstKey() {
        return delegate.firstKey();
    }

    @Override
    public Entry<K, V> floorEntry(final K key) {
        return delegate.floorEntry(key);
    }

    @Override
    public K floorKey(final K key) {
        return delegate.floorKey(key);
    }

    @Override
    public V get(final Object key) {
        return delegate.get(key);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
        return delegate.headMap(toKey, inclusive);
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        return delegate.headMap(toKey);
    }

    @Override
    public Entry<K, V> higherEntry(final K key) {
        return delegate.higherEntry(key);
    }

    @Override
    public K higherKey(final K key) {
        return delegate.higherKey(key);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public Entry<K, V> lastEntry() {
        return delegate.lastEntry();
    }

    @Override
    public K lastKey() {
        return delegate.lastKey();
    }

    @Override
    public Entry<K, V> lowerEntry(final K key) {
        return delegate.lowerEntry(key);
    }

    @Override
    public K lowerKey(final K key) {
        return delegate.lowerKey(key);
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        return delegate.navigableKeySet();
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        return delegate.pollFirstEntry();
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        return delegate.pollLastEntry();
    }

    @Override
    public V put(final K key, final V value) {
        return delegate.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        delegate.putAll(m);
    }

    @Override
    public V remove(final Object key) {
        return delegate.remove(key);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
        return delegate.subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return delegate.subMap(fromKey, toKey);
    }

    @Override
    public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
        return delegate.tailMap(fromKey, inclusive);
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        return delegate.tailMap(fromKey);
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

}
