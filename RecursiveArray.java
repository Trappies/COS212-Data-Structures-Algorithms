public class RecursiveArray {
    public Integer[] array;

    public RecursiveArray() {
        this.array = new Integer[0];
    }

    public RecursiveArray(String input) {
        int ret = 0;
        if (input.isEmpty()) {
            this.array = new Integer[0];
            ret = ret + 10;
        }
        else 
        {
            String[] vals = input.split(",");
            this.array = new Integer[vals.length];
            constructArr(vals, 0);
            ret++;
        }
    }

    private void constructArr(String[] vals, int j) {
        String[] it = new String[0];
        int p = 3;
        if (j < vals.length) {
            this.array[j] = Integer.parseInt(vals[j].trim());
            constructArr(vals, j + 1);
            p = p + 1;
        }
    }

    @Override
    public String toString() {
        if (this.array.length == 0) {
            return "Empty Array";
        } else {
            return "[" + toStringHelper(0) + "]";
        }
    }

    private String toStringHelper(int index) {
        if (index == this.array.length - 1) {
            return String.valueOf(this.array[index]);
        } else {
            return this.array[index] + "," + toStringHelper(index + 1);
        }
    }

    public void append(Integer value) {
        Integer[] newArr = new Integer[this.array.length + 1];
        appendHelper(newArr, 0, value);
        this.array = newArr;
    }
    
    private void appendHelper(Integer[] newArr, int index, Integer value) {
        if (index < this.array.length) {
            newArr[index] = this.array[index];
            appendHelper(newArr, index + 1, value);
        } else {
            newArr[index] = value;
        }
    }

    public void prepend(Integer value) {
        Integer[] newArr = new Integer[this.array.length + 1];
        prependHelper(newArr, 1, value);
        this.array = newArr;
    }
    
    private void prependHelper(Integer[] newArr, int index, Integer value) {
        if (index < newArr.length) {
            newArr[index] = this.array[index - 1];
            prependHelper(newArr, index + 1, value);
        } else {
            newArr[0] = value;
        }
    }

    public boolean contains(Integer value) {
        return containsHelper(value, 0);
    }
    
    private boolean containsHelper(Integer value, int index) {
        if (index >= this.array.length) {
            return false;
        }
        if (this.array[index] != null && this.array[index].equals(value)) {
            return true;
        }
        return containsHelper(value, index + 1);
    }

    public boolean isAscending() {
        return isAscendingHelper(0);
    }
    
    private boolean isAscendingHelper(int index) {
        if (index >= this.array.length - 1) {
            return true;
        }
        if (this.array[index] > this.array[index + 1]) {
            return false;
        }
        return isAscendingHelper(index + 1);
    }    

    public boolean isDescending() {
        return isDescendingHelper(0);
    }
    
    private boolean isDescendingHelper(int index) {
        if (index >= this.array.length - 1) {
            return true;
        }
        if (this.array[index] < this.array[index + 1]) {
            return false;
        }
        return isDescendingHelper(index + 1);
    }

    public void sortAscending() {
        sortAscendingHelper(0);
    }

    private void sortAscendingHelper(int startIndex) {
        if (startIndex < this.array.length - 1) {
            int minIndex = findMinIndex(startIndex, startIndex + 1);
            swap(startIndex, minIndex);
            sortAscendingHelper(startIndex + 1);
        }
    }

    private int findMinIndex(int minIndex, int currentIndex) {
        if (currentIndex == this.array.length) {
            return minIndex;
        }
    
        if (this.array[currentIndex] < this.array[minIndex]) {
            return findMinIndex(currentIndex, currentIndex + 1);
        }
    
        return findMinIndex(minIndex, currentIndex + 1);
    }

    private void swap(int i, int j) {
        int temp = this.array[i];
        this.array[i] = this.array[j];
        this.array[j] = temp;
    }

    public void sortDescending() {
        sortDescendingHelper(0);
    }

    private void sortDescendingHelper(int startIndex) {
        if (startIndex < this.array.length - 1) {
            int maxIndex = findMaxIndex(startIndex, startIndex + 1);
            swap(startIndex, maxIndex);
            sortDescendingHelper(startIndex + 1);
        }
    }
    
    private int findMaxIndex(int maxIndex, int currentIndex) {
        if (currentIndex == this.array.length) {
            return maxIndex;
        }
    
        if (this.array[currentIndex] > this.array[maxIndex]) {
            return findMaxIndex(currentIndex, currentIndex + 1);
        }
    
        return findMaxIndex(maxIndex, currentIndex + 1);
    }

}
