import java.util.ArrayList;
import java.util.List;

public class InnerNode<K extends Comparable<K>, V> implements INode<K, V> {

    private List<K> keys;
    private List<INode<K, V>> children;

    public InnerNode(int order) {
        this.keys = new ArrayList<>(order);
        this.children = new ArrayList<>(order + 1);
    }
    public int getIndex(K key) {
        for (int i = 0; i < keys.size(); i++) {
            int cmp = key.compareTo(keys.get(i));
            if (cmp < 0) {
                return i;
            }
        }
        return keys.size();
    }

    public List<INode<K, V>> getChildren() {
        return children;
    }

    @Override
    public void insert(K key, V value) {
        int i = getChildIndex(key);
        INode<K, V> child = children.get(i);
        child.insert(key, value);
        if (child.isOverflow()) {
            List<INode<K, V>> newChildren = child.split();
            keys.add(i, newChildren.get(0).getKeys().get(0));
            children.remove(i);
            children.addAll(i, newChildren);
        }
    }

    @Override
    public V get(K key) {
        int i = getChildIndex(key);
        return children.get(i).get(key);
    }

    @Override
    public void remove(K key) {
        int i = getChildIndex(key);
        INode<K, V> child = children.get(i);
        child.remove(key);
        if (child.isUnderflow()) {
            if (i == 0) {
                if (children.get(i + 1).getKeys().size() > getOrder() / 2) {
                    redistributeRight(i);
                } else {
                    mergeWithNext(i);
                }
            } else if (i == children.size() - 1) {
                if (children.get(i - 1).getKeys().size() > getOrder() / 2) {
                    redistributeLeft(i);
                } else {
                    mergeWithNext(i - 1);
                }
            } else {
                if (children.get(i - 1).getKeys().size() > getOrder() / 2) {
                    redistributeLeft(i);
                } else if (children.get(i + 1).getKeys().size() > getOrder() / 2) {
                    redistributeRight(i);
                } else {
                    mergeWithNext(i);
                }
            }
        }
    }

    private void redistributeLeft(int i) {
        INode<K, V> child = children.get(i);
        INode<K, V> leftSibling = children.get(i - 1);
        child.getKeys().add(0, keys.get(i - 1));
        keys.set(i - 1, leftSibling.getKeys().remove(leftSibling.getKeys().size() - 1));
        child.getChildren().add(0, leftSibling.getChildren().remove(leftSibling.getChildren().size() - 1));
    }

    private void redistributeRight(int i) {
        INode<K, V> child = children.get(i);
        INode<K, V> rightSibling = children.get(i + 1);
        child.getKeys().add(keys.get(i));
        keys.set(i, rightSibling.getKeys().remove(0));
        child.getChildren().add(rightSibling.getChildren().remove(0));
    }

    private void mergeWithNext(int i) {
        INode<K, V> child = children.get(i);
        INode<K, V> rightSibling = children.get(i + 1);
        child.getKeys().add(keys.remove(i));
        child.getKeys().addAll(rightSibling.getKeys());
        child.getChildren().addAll(rightSibling.getChildren());
        children.remove(i + 1);
    }

    @Override
    public boolean isOverflow() {
        return keys.size() >= getOrder();
    }

    @Override
    public boolean isUnderflow() {
        return keys.size() < getOrder() / 2;
    }

    @Override
    public List<INode<K, V>> split() {
        int mid = keys.size() / 2;
        InnerNode<K, V> newInner = new InnerNode<>(int order);
        newInner.keys.addAll(keys.subList(mid + 1, keys.size()));
        newInner.children.addAll(children.subList(mid + 1, children.size()));
        keys.subList(mid, keys.size()).clear();
        children.subList(mid + 1, children.size()).clear();
        List<INode<K, V>> result = new ArrayList<>();
        result.add(newInner);
        result.add(this);
        return result;
    }


}
