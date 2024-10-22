
public class Hashmap<K, V> {
    public HashNode<K, V>[] data;
    public int numValues;
    public int capacity;

    public Hashmap() {
        this.data = (HashNode<K, V>[]) new HashNode[2];
        this.numValues = 0;
        this.capacity = 2;
    }

    public boolean insert(K key, V value) {
        int hash = hornerHash(key);
        int step = secondaryHash(key);
        int index = hash % capacity;
    
        while (data[index] != null && !data[index].key.equals(key)) {
            index = (index + step) % capacity;
        }
    
        if (data[index] != null && data[index].key.equals(key)) {
            return false;
        }
    
        data[index] = new HashNode<>(key, value);
        numValues++;
    
        if (numValues >= capacity) {
            rehash();
        }
    
        return true;
    }
    
    private void rehash() {
        capacity *= 2; 
        HashNode<K, V>[] newData = (HashNode<K, V>[]) new HashNode[capacity];
    
        for (HashNode<K, V> node : data) {
            if (node != null) {
                int hash = hornerHash(node.key);
                int step = secondaryHash(node.key);
                int index = hash % capacity;
    
                while (newData[index] != null) {
                    index = (index + step) % capacity;
                }
    
                newData[index] = node;
            }
        }
    
        data = newData;
    }
    

    public void delete(K key) {
        int hash = hornerHash(key);
        int step = secondaryHash(key);
        int index = hash % capacity;
        int startIndex = index;
    
        do {
            if (data[index] != null && data[index].key.equals(key)) {
                data[index] = null;
                numValues--;
                return;
            }
            index = (index + step) % capacity;
        } while (index != startIndex);
    }
    

    public V get(K key) {
        int hash = hornerHash(key);
        int step = secondaryHash(key);
        int index = hash % capacity;
        int startIndex = index;
    
        do {
            HashNode<K, V> currentNode = data[index];
            if (currentNode != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            index = (index + step) % capacity;
        } while (index != startIndex);
    
        return null;
    }
    

    public Object[] getKeys() {
        Object[] keysArray = new Object[numValues];
        int count = 0;
        for (HashNode<K, V> node : data) {
            if (node != null) {
                keysArray[count] = node.key;
                count++;
            }
        }

        return keysArray;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            data[i] = null;
        }
        capacity = 2;
        numValues = 0;
    }

    /* -------------------------------------------------------------------------- */
    /* Please don't change this toString, I tried to make it pretty. */
    /* -------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------- */
    /* Also we may test against it */
    /* -------------------------------------------------------------------------- */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String header = String.format("%-10s | %-20s | %-20s%n", "Index", "Key", "Value");
        sb.append(header);
        for (int i = 0; i < header.length() - 1; i++) {
            sb.append("-");
        }
        sb.append("\n");
        for (int i = 0; i < capacity; i++) {
            if (data[i] != null) {
                String row = String.format("%-10d | %-20s | %-20s%n", i, data[i].key.toString(),
                        data[i].value.toString());
                sb.append(row);
            } else {
                String row = String.format("%-10d | %-20s | %-20s%n", i, "null", "null");
                sb.append(row);
            }
        }

        return sb.toString();
    }

    public int hornerHash(K key) {
        String keyStr = key.toString();
        int hashVal = 0;
        for (int i = 0; i < keyStr.length(); i++)
            hashVal = 37 * hashVal + keyStr.charAt(i);
        hashVal %= capacity;
        if (hashVal < 0)
            hashVal += capacity;
        return hashVal;
    }

    public int secondaryHash(K key) {
        int hash = key.hashCode();
        // Ensure the step size is odd to ensure it's coprime with capacity, since
        // capacity is a power of 2.
        int step = (hash & (capacity - 1)) | 1; // This ensures the step size is always odd.
        return step;
    }

}
