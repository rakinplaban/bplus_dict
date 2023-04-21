import java.util.*;
public interface INode<K extends Comparable<K>, V> {
    void insert(K key, V value);
    V get(K key);

    void put(K key, V value);

    void remove(K key);
    boolean isOverflow();
    boolean isUnderflow();
    List<INode<K, V>> split();

}
