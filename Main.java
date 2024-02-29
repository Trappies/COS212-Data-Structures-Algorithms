public class Main {
    public static void main(String[] args) {
        RecursiveArray emptyArray = new RecursiveArray();
        System.out.println("Empty Arr: " + emptyArray.toString());

        RecursiveArray arrayFromInput = new RecursiveArray("1,2,3,4,5");
        System.out.println("Input Arr: " + arrayFromInput.toString());

        arrayFromInput.append(6);
        System.out.println("Append Arr: " + arrayFromInput.toString());

        arrayFromInput.prepend(0);
        System.out.println("Prepend Arr: " + arrayFromInput.toString());

        System.out.println("Arr containing 3: " + arrayFromInput.contains(3));
        System.out.println("Arr containing 10: " + arrayFromInput.contains(10));

        System.out.println("Asc Arr: " + arrayFromInput.isAscending());

        arrayFromInput.sortAscending();
        System.out.println("Sorted Arr Asc: " + arrayFromInput.toString());

        System.out.println("Desc Arr: " + arrayFromInput.isDescending());

        arrayFromInput.sortDescending();
        System.out.println("Sorted Arr Desc: " + arrayFromInput.toString());

        System.out.println("Desc Arr: " + arrayFromInput.isDescending());
    }
}
