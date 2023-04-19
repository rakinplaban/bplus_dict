import java.util.ArrayList;
import java.util.List;

public class BPlusTree<K extends Comparable<K>, V> {
    private int order; // maximum number of keys in each node
    private INode root; // root node of the B+ tree

    // constructor
    public BPlusTree(int order) {
        this.order = order;
        root = new LeafNode();
    }

    // put a key-value pair into the tree
    public void put(K key, V value) {
        root.put(key, value);
        if (root.isOverflow()) {
            INode newRoot = new InnerNode();
            newRoot.addChild(root.split());
            newRoot.addChild(root);
            root = newRoot;
        }
    }

    // get the value associated with a key
    public V get(K key) {
        return root.get(key);
    }

    // remove the key-value pair associated with a key
    public void remove(K key) {
        root.remove(key);
        if (root instanceof InnerNode && root.getChildren().size() == 1) {
            root = root.getChildren().get(0);
        }
    }