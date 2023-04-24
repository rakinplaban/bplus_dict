import java.util.*;
import java.lang.Object.*;
import java.io.*;

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
        try (FileWriter writer = new FileWriter("wordlist.txt", true)) {
            writer.write(key.toString() + " : " + value.toString() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    public V search(K key) {
        try {
            Scanner scanner = new Scanner(new File("wordlist.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    K fileKey = (K) parts[0].trim();
                    V fileValue = (V) parts[1].trim();
                    if (fileKey.equals(key)) {
                        if (fileValue == null) {
                            throw new NullPointerException("This word has no meaning!");
                        } else {
                            scanner.close();
                            return fileValue;
                        }
                    }
                }
            }
            scanner.close();
            System.out.println("Word not found!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return null;
    }

}