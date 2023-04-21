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
        root.put(key, value);
        if (root.isOverflow()) {
            INode newRoot = new InnerNode(5);
            newRoot.addChild(root.split());
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
            root = root.getChildren().get(0);
        }
    }

    public void insert(K key, V value) {
        // Find the leaf node where the key should be inserted
        LeafNode<K, V> leafNode = findLeafNode(key);

        // Insert the key-value pair into the leaf node
        leafNode.insert(key, value);

        // If the leaf node is overflowing, split it and propagate the split upwards
        if (leafNode.isOverflow()) {
            List<INode<K, V>> newNodes = leafNode.split();

            // Insert the new nodes into the parent node
            InnerNode<K, V> parentNode = leafNode.getParent();
            while (parentNode != null) {
                int index = parentNode.addChild(newNodes.get(0));
                if (newNodes.size() == 1) {
                    parentNode.setKey(index, newNodes.get(0).getKeys().get(0));
                    break;
                } else {
                    parentNode.setKey(index, newNodes.get(1).getKeys().get(0));
                    newNodes.remove(0);
                    newNodes.remove(0);
                }
                if (parentNode.isOverflow()) {
                    newNodes = parentNode.split();
                    parentNode = parentNode.getParent();
                } else {
                    break;
                }
            }

            // If the root node has split, create a new root node
            if (parentNode == null) {
                InnerNode<K, V> newRootNode = new InnerNode<>(order);
                newRootNode.addChild(leafNode);
                newRootNode.addChild(newNodes.get(0));
                newRootNode.setKey(0, newNodes.get(0).getKeys().get(0));
                root = newRootNode;
            }
        }
    }

    public V search(V key) {
        LeafNode<K, V> leafNode = findLeafNode((K) key);
        return leafNode.get((K) key);
    }
}