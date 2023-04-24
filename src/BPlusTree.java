import java.util.ArrayList;
import java.util.List;
import java.lang.Object.*;

public class BPlusTree<K extends Comparable<K>, V> {
    private int order; // maximum number of keys in each node
    private INode root; // root node of the B+ tree

    // constructor
    public BPlusTree(int order) {
        this.order = order;
        root = new LeafNode(order);
    }
    private LeafNode<K, V> findLeafNode(K key) {
        INode<K, V> node = root;

        while (!(node instanceof LeafNode)) {
            InnerNode<K, V> innerNode = (InnerNode<K, V>) node;
            int index = innerNode.getIndex(key);
            node = innerNode.getChildren().get(index);
        }

        return (LeafNode<K, V>) node;
    }

    // put a key-value pair into the tree

    public void put(K key, V value) {
        if (root == null) {
            root = new LeafNode<>(order);
        }
        root.put(key, value);
        if (root.isOverflow()) {
            List<INode<K, V>> newNodes = root.split();
            INode<K, V> newRoot = new InnerNode<>(order);
            for (INode<K, V> node : newNodes) {
                newRoot.addChild(node);
            }
            newRoot.addChild(root);
            root = newRoot;
        }
    }



    // get the value associated with a key
    public V get(K key) {
        return (V) root.get(key);
    }

    // remove the key-value pair associated with a key
    public void remove(K key) {
        root.remove(key);
        if (root instanceof InnerNode && root.getChildren().size() == 1) {
            root = (INode) root.getChildren().get(0);
        }
    }


    public void insert(K key, V value) {
        root.insert(key, value);
        if (root.isOverflow()) {
            List<INode<K, V>> newNodes = root.split();
            InnerNode<K, V> newRoot = new InnerNode<>(order);
            newRoot.addChild(newNodes.get(0));
            newRoot.addChild(newNodes.get(1));
            root = newRoot;
        }
    }


    public V search(V key) {
        LeafNode<K, V> leafNode = findLeafNode((K) key);
        return leafNode.get((K) key);
    }
}