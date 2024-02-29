public class Main {
    // Output is in output.txt
    public static void main(String[] args) {
        Maze m = new Maze("input.txt");
        // System.out.println(m.toString());
        System.out.println(m.solve(3, 3, 1, 0));
        System.out.println(m.validStarts(0, 0));
        
    }
}