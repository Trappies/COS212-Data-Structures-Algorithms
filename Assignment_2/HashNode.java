public class HashNode<K, V> {
    public K key;
    public V value;

    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return "{" + key.toString() + ": " + value.toString() + "}";
    }
}
