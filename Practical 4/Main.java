public class Main {
    public static void main(String[] args) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        tree.bottomUpInsert(10);
        tree.bottomUpInsert(5);
        tree.bottomUpInsert(15);
        tree.bottomUpInsert(3);
        tree.bottomUpInsert(7);
        tree.bottomUpInsert(12);
        tree.bottomUpInsert(18);

        System.out.println("Insert into RB-Tree:");
        System.out.println(tree);
        System.out.println("Is it a valid tree?: " + tree.isValidRedBlackTree());
        System.out.println();

        System.out.println("Insert duplicate 5");
        tree.bottomUpInsert(5);

        System.out.println("Insert duplicate into RB-Tree");
        System.out.println(tree);
        System.out.println("Is it a valid tree?: " + tree.isValidRedBlackTree());
        System.out.println();

        System.out.println("Deleting 10");
        tree.topDownDelete(10);

        System.out.println("RB Tree after deletion:");
        System.out.println(tree);
        System.out.println("Is it a valid tree?: " + tree.isValidRedBlackTree());
        System.out.println();

        System.out.println("Deleting 5");
        tree.topDownDelete(5);

        System.out.println("RB Tree after deletion:");
        System.out.println(tree);
        System.out.println("Is it a valid tree?: " + tree.isValidRedBlackTree());
        System.out.println();

        System.out.println("Deleting 15");
        tree.topDownDelete(15);

        System.out.println("RB Tree after deletion:");
        System.out.println(tree);
        System.out.println("Is it a valid tree?: " + tree.isValidRedBlackTree());
        System.out.println();

        System.out.println("Deleting 3");
        tree.topDownDelete(3);

        System.out.println("RB Tree after deletion:");
        System.out.println(tree);
        System.out.println("Is it a valid tree?: " + tree.isValidRedBlackTree());
        System.out.println();

        System.out.println("Deleting node not in tree");
        tree.topDownDelete(20);

        System.out.println("Deleting non-existent node in RB-Tree");
        System.out.println(tree);
        System.out.println("Is it a valid tree?: " + tree.isValidRedBlackTree());
    }
}
