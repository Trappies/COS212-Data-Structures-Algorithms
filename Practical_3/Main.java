public class Main {
    public static void main(String[] args) {
        NodeTester();
        SplayTreeTester();
    }

    public static void NodeTester() {
        Node node = new Node(1, 1);
        System.out.println(node.toString());
        node.left = new Node(2, 2);
        System.out.println(node.toString());
        node.right = new Node(3, 3);
        System.out.println(node.toString());
        node.compareTo(node);
    }

    public static void SplayTreeTester() {
        String input1 = "{[u10:50%]{}{}}";
        String input2 = "{[u10:50%]{[u5:80%]{}{}}{[u15:60%]{}{}}}";
        String input3 = "Empty Tree";
        String input4 = "{[u3:54%]{[u2:null%]{[u1:1%]{}{}}{}}{[u212141:71%]{}{[u212145:90%]{}{}}}}";

        SplayTree tree1 = new SplayTree(input1);
        SplayTree tree2 = new SplayTree(input2);
        SplayTree tree3 = new SplayTree(input3);

        System.out.println("Tree 1: " + tree1.toStringOneLine());
        System.out.println("Tree 2: " + tree2.toStringOneLine());
        System.out.println("Tree 3: " + tree3.toStringOneLine());

        tree1.access(20, 30);

        System.out.println("\n\nTree 1 with Node [u20:30%]: \n" + tree1.toString());

        tree2.access(20, 30);

        System.out.println("\n\nTree 2 with Node [u20:30%]: \n" + tree2.toString());

        tree2.access(13, null);

        System.out.println("\n\nTree 2 with Node [u13:null]: \n" + tree2.toString());

        tree2.remove(15);
        System.out.println("\n\nDelete u15: \n" + tree2.toString());
        tree2.remove(10);
        System.out.println("\n\nDelete u10: \n" + tree2.toString());

        SplayTree tree4 = new SplayTree(input2);
        tree4.remove(15);
        System.out.println("\n\nDelete u15: \n" + tree4.toString());
        tree4.remove(10);
        System.out.println("\n\nDelete u10: \n" + tree4.toString());
        tree4.remove(13);
        System.out.println("\n\nDelete u13: \n" + tree4.toString());
        tree4.remove(5);
        System.out.println("\n\nDelete u5: \n" + tree4.toString());

        SplayTree tree5 = new SplayTree(input2);
        System.out.println("\n\nTree5 Sorted By Student Number: \n" + tree5.sortByStudentNumber());
        System.out.println("\n\nTree3 Sorted By Student Number: \n" + tree3.sortByStudentNumber());

        SplayTree tree6 = new SplayTree(input2);
        System.out.println("\n\nTree5 Sorted By Marks: \n" + tree6.sortByMark());
        System.out.println("\n\nTree3 Sorted By Marks: \n" + tree3.sortByMark());

        SplayTree nullTree = new SplayTree(input4);
        System.out.println("\n\nNull Tree: \n" + nullTree.toString());
        nullTree.remove(3);
        System.out.println("\n\nNull Tree remove u3: \n" + nullTree.toString());
        nullTree.remove(212145);
        System.out.println("\n\nNull Tree remove u212145: \n" + nullTree.toString());

        tree3.access(33, null);
        System.out.println("\n\nTree 3 with Node [u33:null%]: \n" + tree3.toString());

    }
}