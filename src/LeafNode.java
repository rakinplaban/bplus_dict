import java.util.ArrayList;
import java.util.List;

public class LeafNode<K extends Comparable<K>, V> implements INode<K, V> {
    private List<K> keys;
    private List<V> values;
    private LeafNode<K, V> next;
    private int order;
    private LeafNode<K,V> prev;

    public LeafNode(int order) {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
        this.next = null;
        this.order = order;
    }

    public List<K> getKeys() {
        return keys;
    }

    public List<V> getValues() {
        return values;
    }

    public LeafNode<K, V> getNext() {
        return next;
    }

    public void setNext(LeafNode<K, V> next) {
        this.next = next;
    }

    public boolean isOverflow() {
        return keys.size() > order - 1;
    }

    public boolean isUnderflow() {
        return keys.size() < (order - 1) / 2;
    }

    public int getChildIndex(K key) {
        for (int i = 0; i < keys.size(); i++) {
            if (key.compareTo(keys.get(i)) <= 0) {
                return i;
            }
        }
        return keys.size();
    }

    public V search(K key) {
        int i = getChildIndex(key);
        if (i < keys.size() && keys.get(i).equals(key)) {
            return values.get(i);
        }
        return null;
    }

    public void insert(K key, V value) {
        int i = getChildIndex(key);
        keys.add(i, key);
        values.add(i, value);
    }
    @Override
    public V get(K key) {
        int i = getChildIndex(key);
        if (i < keys.size() && keys.get(i).equals(key)) {
            return values.get(i);
        } else {
            return null;
        }
    }

    @Override
    public List<INode<K, V>> split() {
        int mid = keys.size() / 2;
        LeafNode<K, V> newLeaf = new LeafNode<>(order);
        newLeaf.keys.addAll(keys.subList(mid, keys.size()));
        newLeaf.values.addAll(values.subList(mid, values.size()));
        keys.subList(mid, keys.size()).clear();
        values.subList(mid, values.size()).clear();
        if (next != null) {
            newLeaf.next = next;
            next.prev = newLeaf;
        }
        next = newLeaf;
        newLeaf.prev = this;
        List<INode<K, V>> result = new ArrayList<>();
        result.add(newLeaf);
        result.add(this);
        return result;
    }


    public void remove(K key) {
        int i = getChildIndex(key);
        if (i < keys.size() && keys.get(i).equals(key)) {
            keys.remove(i);
            values.remove(i);
        }
    }

}
