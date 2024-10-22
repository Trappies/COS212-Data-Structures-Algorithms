import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze {
    List<Vertex> vertices;
    List<Edge> edges;
    Vertex start;

    Maze() {
        start = null;
        edges = new List<>();
        vertices = new List<>();
    }

    Maze(String fileName) {
        vertices = new List<>();
        edges = new List<>();
        start = null;

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            Vertex[][] vertexGrid = null;
            int row = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (vertexGrid == null) {
                    vertexGrid = new Vertex[line.length()][];
                }

                vertexGrid[row] = new Vertex[line.length()];
                
                for (int col = 0; col < line.length(); col++) {
                    char symbol = line.charAt(col);
                    if (symbol != '#') {
                        Vertex vertex = new Vertex(col, row, symbol);
                        vertices.add(vertex);
                        vertexGrid[row][col] = vertex;

                        if (start == null && symbol == 'S') {
                            start = vertex;
                        }

                        if (row > 0 && vertexGrid[row - 1][col] != null) {
                            addEdge(vertex, vertexGrid[row - 1][col]);
                        }
                        if (col > 0 && vertexGrid[row][col - 1] != null) {
                            addEdge(vertex, vertexGrid[row][col - 1]);
                        }
                    }
                }
                row++;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    private void addEdge(Vertex v1, Vertex v2) {
        Edge edge = new Edge(v1, v2, 1);
        edges.add(edge);
        v1.addEdge(edge);
        v2.addEdge(edge);
    }

    private void addAdjacentEdges(Vertex vertex, int row, int col, String line) {
        if (row > 0 && line.charAt(col) != '#') {
            Vertex north = getVertex(new Vertex(col, row - 1, line.charAt(col)));
            if (north != null) {
                Edge edge = new Edge(vertex, north, 1);
                edges.add(edge);
                vertex.addEdge(edge);
                north.addEdge(edge);
            }
        }

        if (row < line.length() - 1 && line.charAt(col) != '#') {
            Vertex south = getVertex(new Vertex(col, row + 1, line.charAt(col)));
            if (south != null) {
                Edge edge = new Edge(vertex, south, 1);
                edges.add(edge);
                vertex.addEdge(edge);
                south.addEdge(edge);
            }
        }

        if (col > 0 && line.charAt(col - 1) != '#') {
            Vertex west = getVertex(new Vertex(col - 1, row, line.charAt(col - 1)));
            if (west != null) {
                Edge edge = new Edge(vertex, west, 1);
                edges.add(edge);
                vertex.addEdge(edge);
                west.addEdge(edge);
            }
        }

        if (col < line.length() - 1 && line.charAt(col + 1) != '#') {
            Vertex east = getVertex(new Vertex(col + 1, row, line.charAt(col + 1)));
            if (east != null) {
                Edge edge = new Edge(vertex, east, 1);
                edges.add(edge);
                vertex.addEdge(edge);
                east.addEdge(edge);
            }
        }
    }
    

    static Maze createMaze(String mazeString) {
        Maze maze = new Maze();

    String[] rows = mazeString.split("\n");
    Vertex[][] vertexGrid = new Vertex[rows.length][];
    
    for (int row = 0; row < rows.length; row++) {
        String line = rows[row];
        vertexGrid[row] = new Vertex[line.length()];
        
        for (int col = 0; col < line.length(); col++) {
            char symbol = line.charAt(col);
            if (symbol != '#') {
                Vertex vertex = new Vertex(col, row, symbol);
                maze.vertices.add(vertex);
                vertexGrid[row][col] = vertex;

                if (maze.start == null && symbol == 'S') {
                    maze.start = vertex;
                }

                if (row > 0 && vertexGrid[row - 1][col] != null) {
                    maze.addEdge(vertex, vertexGrid[row - 1][col]);
                }
                if (col > 0 && vertexGrid[row][col - 1] != null) {
                    maze.addEdge(vertex, vertexGrid[row][col - 1]);
                }
            }
        }
    }

    return maze;
    }

    String latexCode() {
        String result = "\\documentclass[hidelinks, 12pt, a4paper]{article}\r\n" + //
                "\\usepackage{tikz}\n" + //
                "\n" + //
                "\\begin{document}\n" + //
                "\n" + //
                "\\begin{tikzpicture}[node/.style={circle, draw, minimum size=1.2em},yscale=-1]\n";
        for (Vertex v : getVertices()) {
            result += v.latexCode() + "\n";
        }
        result += "\n";
        for (Edge e : getEdges()) {
            result += e.latexCode() + "\n";
        }
        result += "\\end{tikzpicture}\r\n" + //
                "\r\n" + //
                "\\end{document}";
        return result;
    }

    Vertex[] getVertices() {
        Vertex[] vertexArray = new Vertex[vertices.getSize()];
        return vertices.toArray(vertexArray);
    }

    Edge[] getEdges() {
        List<Edge> uniqueEdges = new List<>();
        List.Node<Edge> current = edges.getHead();
        while (current != null) {
            Edge edge = current.getData();
            if (!uniqueEdges.contains(edge)) {
                uniqueEdges.add(edge);
            }
            current = current.getNext();
        }
        return uniqueEdges.toArray(new Edge[uniqueEdges.getSize()]);
    }

    void stage1Reducing() {
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Vertex> toRemove = new List<>();
            List<Edge> toRemoveEdges = new List<>();
    
            List.Node<Vertex> current = vertices.getHead();
            while (current != null) {
                Vertex vertex = current.getData();
                if (vertex != null && vertex.getSymbol() == ' ' && vertex.getEdges().length == 2) {
                    toRemove.add(vertex);
                    toRemoveEdges.add(vertex.getEdges()[0]);
                    toRemoveEdges.add(vertex.getEdges()[1]);
                    changed = true;
                }
                current = current.getNext();
            }
    
            List.Node<Vertex> removeNode = toRemove.getHead();
            while (removeNode != null) {
                Vertex vertex = removeNode.getData();
                if (vertex != null) {
                    vertices.remove(vertex);
                    Edge[] edges = vertex.getEdges();
                    Vertex v1 = null, v2 = null;
    
                    for (Edge edge : edges) {
                        Vertex v = (edge.v1.equals(vertex)) ? edge.v2 : edge.v1;
                        if (v1 == null) {
                            v1 = v;
                        } else {
                            v2 = v;
                        }
                    }
    
                    if (v1 != null && v2 != null) {
                        double weight = edges[0].weight + edges[1].weight;
                        Edge newEdge = new Edge(v1, v2, weight);
                        this.edges.add(newEdge);
                        v1.addEdge(newEdge);
                        v2.addEdge(newEdge);
                    }
                }
                removeNode = removeNode.getNext();
            }
    
            List.Node<Edge> edgeNode = toRemoveEdges.getHead();
            while (edgeNode != null) {
                Edge edge = edgeNode.getData();
                if (edge != null) {
                    edges.remove(edge);
                }
                edgeNode = edgeNode.getNext();
            }
        }
    }
    

    void stage2Reducing() {// Removing dead ends
        List<Vertex> toRemove = new List<>();
    List<Edge> toRemoveEdges = new List<>();

    List.Node<Vertex> current = vertices.getHead();
    while (current != null) {
        Vertex vertex = current.getData();
        if (vertex != null) {
            if (vertex.getSymbol() == ' ' && vertex.getEdges().length == 1) {
                toRemove.add(vertex);
                toRemoveEdges.add(vertex.getEdges()[0]);
            } else if (vertex.getEdges().length == 0) {
                toRemove.add(vertex);
            }
        }
        current = current.getNext();
    }

    List.Node<Vertex> removeNode = toRemove.getHead();
    while (removeNode != null) {
        Vertex vertex = removeNode.getData();
        if (vertex != null) {
            vertices.remove(vertex);
        }
        removeNode = removeNode.getNext();
    }

    List.Node<Edge> edgeNode = toRemoveEdges.getHead();
    while (edgeNode != null) {
        Edge edge = edgeNode.getData();
        if (edge != null) {
            edges.remove(edge);
        }
        edgeNode = edgeNode.getNext();
    }
    }

    void stage3Reducing() {// Doubling the weight to traps
        List<Edge> toUpdate = new List<>();

        List.Node<Vertex> current = vertices.getHead();
        while (current != null) {
            Vertex vertex = current.getData();
            if (vertex.getSymbol() == 'T') {
                for (Edge edge : vertex.getEdges()) {
                    toUpdate.add(edge);
                }
            }
            current = current.getNext();
        }

        List.Node<Edge> edgeNode = toUpdate.getHead();
        while (edgeNode != null) {
            Edge edge = edgeNode.getData();
            edge.setWeight(edge.getWeight() * 2);
            edgeNode = edgeNode.getNext();
        }
    }

    public Vertex[] getAllDoors() {
        List<Vertex> doors = new List<>();
        List.Node<Vertex> current = vertices.getHead();
        while (current != null) {
            Vertex vertex = current.getData();
            if (vertex.getSymbol() == 'D') {
                doors.add(vertex);
            }
            current = current.getNext();
        }
        return doors.toArray(new Vertex[doors.getSize()]);
    }

    public Vertex[] getAllGoals() {
        List<Vertex> goals = new List<>();
        List.Node<Vertex> current = vertices.getHead();
        while (current != null) {
            Vertex vertex = current.getData();
            char symbol = vertex.getSymbol();
            if (Character.isDigit(symbol)) {
                goals.add(vertex);
            }
            current = current.getNext();
        }
        return goals.toArray(new Vertex[goals.getSize()]);
    }

    public Vertex[] getAllKeys() {
        List<Vertex> keys = new List<>();
        List.Node<Vertex> current = vertices.getHead();
        while (current != null) {
            Vertex vertex = current.getData();
            char symbol = vertex.getSymbol();
            if (Character.isLetter(symbol)) {
                keys.add(vertex);
            }
            current = current.getNext();
        }
        return keys.toArray(new Vertex[keys.getSize()]);
    }

    boolean isReachAble(Vertex start, Vertex goal) {
        if (start == null || goal == null) {
            return false;
        }

        List<Vertex> visited = new List<>();
        return isReachAbleHelper(start, goal, visited);
    }

    private boolean isReachAbleHelper(Vertex current, Vertex goal, List<Vertex> visited) {
        if (current.equals(goal)) {
            return true;
        }

        visited.add(current);

        Edge[] edges = current.getEdges();
        sortEdges(edges);

        for (Edge edge : edges) {
            Vertex neighbor = getOtherVertex(current, edge);
            if (neighbor != null && neighbor.getSymbol() != 'D' && !visited.contains(neighbor)) {
                if (isReachAbleHelper(neighbor, goal, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Vertex getOtherVertex(Vertex current, Edge edge) {
        Vertex v1 = edge.v1;
        Vertex v2 = edge.v2;
        if (v1.equals(current)) {
            return v2;
        } else if (v2.equals(current)) {
            return v1;
        }
        return null;
    }

    private void sortEdges(Edge[] edges) {
        for (int i = 0; i < edges.length - 1; i++) {
            for (int j = 0; j < edges.length - i - 1; j++) {
                if (edges[j].toString().compareTo(edges[j + 1].toString()) > 0) {
                    Edge temp = edges[j];
                    edges[j] = edges[j + 1];
                    edges[j + 1] = temp;
                }
            }
        }
    }

    Vertex[] isReachAblePath(Vertex start, Vertex goal) {
        if (start == null || goal == null) {
            return new Vertex[0];
        }

        List<Vertex> visited = new List<>();
        List<Vertex> path = new List<>();
        boolean found = isReachAblePathHelper(start, goal, visited, path);

        if (found) {
            return path.toArray(new Vertex[path.getSize()]);
        } else {
            return new Vertex[0];
        }
    }

    private boolean isReachAblePathHelper(Vertex current, Vertex goal, List<Vertex> visited, List<Vertex> path) {
        visited.add(current);
        path.add(current);

        if (current.equals(goal)) {
            return true;
        }

        Edge[] edges = current.getEdges();
        sortEdges(edges);

        for (Edge edge : edges) {
            Vertex neighbor = getOtherVertex(current, edge);
            if (neighbor != null && neighbor.getSymbol() != 'D' && !visited.contains(neighbor)) {
                if (isReachAblePathHelper(neighbor, goal, visited, path)) {
                    return true;
                }
            }
        }

        path.remove(current);
        return false;
    }

    double shortestPathDistanceNoDoor(Vertex start, Vertex goal) {
        if (start == null || goal == null || start.getSymbol() == 'D') {
            return Double.POSITIVE_INFINITY;
        }

        List<Vertex> visited = new List<>();
        return shortestPathDistanceNoDoorHelper(start, goal, visited);
    }

    private double shortestPathDistanceNoDoorHelper(Vertex current, Vertex goal, List<Vertex> visited) {
        visited.add(current);

        if (current.equals(goal)) {
            return 0;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Edge[] edges = current.getEdges();
        sortEdges(edges);

        for (Edge edge : edges) {
            Vertex neighbor = getOtherVertex(current, edge);
            if (neighbor != null && neighbor.getSymbol() != 'D' && !visited.contains(neighbor)) {
                double distance = shortestPathDistanceNoDoorHelper(neighbor, goal, visited);
                if (distance != Double.POSITIVE_INFINITY) {
                    distance += edge.getWeight();
                    minDistance = Math.min(minDistance, distance);
                }
            }
        }

        visited.remove(current);
        return minDistance;
    }

    Vertex[] shortestPathPathNoDoor(Vertex start, Vertex goal) {
        if (start == null || goal == null || start.getSymbol() == 'D') {
            return new Vertex[0];
        }

        List<Vertex> visited = new List<>();
        List<Vertex> path = new List<>();
        boolean found = shortestPathPathNoDoorHelper(start, goal, visited, path);

        if (found) {
            return path.toArray(new Vertex[0]);//path.getSize()
        } else {
            return new Vertex[0];
        }
    }

    private boolean shortestPathPathNoDoorHelper(Vertex current, Vertex goal, List<Vertex> visited, List<Vertex> path) {
        visited.add(current);
        path.add(current);

        if (current.equals(goal)) {
            return true;
        }

        Edge[] edges = current.getEdges();
        sortEdges(edges);

        for (Edge edge : edges) {
            Vertex neighbor = getOtherVertex(current, edge);
            if (neighbor != null && neighbor.getSymbol() != 'D' && !visited.contains(neighbor)) {
                if (shortestPathPathNoDoorHelper(neighbor, goal, visited, path)) {
                    return true;
                }
            }
        }

        path.remove(current);
        visited.remove(current);
        return false;
    }

    double shortestPathDistanceDoor(Vertex start, Vertex goal, boolean goUp) {
        if (start == null || goal == null || start.getSymbol() != 'D') {
            return Double.POSITIVE_INFINITY;
        }

        List<Vertex> visited = new List<>();
        return shortestPathDistanceDoorHelper(start, goal, goUp, visited);
    }

    private double shortestPathDistanceDoorHelper(Vertex current, Vertex goal, boolean goUp, List<Vertex> visited) {
        visited.add(current);

        if (current.equals(goal)) {
            return 0;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Edge[] edges = current.getEdges();
        sortEdges(edges);

        for (Edge edge : edges) {
            Vertex neighbor = getOtherVertex(current, edge);
            if (neighbor != null && !visited.contains(neighbor)) {
                if (goUp && neighbor.getYPos() >= current.getYPos()
                        || !goUp && neighbor.getYPos() <= current.getYPos()) {
                    double distance = shortestPathDistanceDoorHelper(neighbor, goal, goUp, visited);
                    if (distance != Double.POSITIVE_INFINITY) {
                        distance += edge.getWeight();
                        minDistance = Math.min(minDistance, distance);
                    }
                }
            }
        }

        visited.remove(current);
        return minDistance;
    }

    Vertex[] shortestPathPathDoor(Vertex start, Vertex goal, boolean goUp) {
        if (start == null || goal == null || start.getSymbol() != 'D') {
            return new Vertex[0];
        }

        List<Vertex> visited = new List<>();
        List<Vertex> path = new List<>();
        boolean found = shortestPathPathDoorHelper(start, goal, goUp, visited, path);

        if (found) {
            return path.toArray(new Vertex[path.getSize()]);
        } else {
            return new Vertex[0];
        }
    }

    private boolean shortestPathPathDoorHelper(Vertex current, Vertex goal, boolean goUp, List<Vertex> visited,
            List<Vertex> path) {
        visited.add(current);
        path.add(current);

        if (current.equals(goal)) {
            return true;
        }

        Edge[] edges = current.getEdges();
        sortEdges(edges);

        for (Edge edge : edges) {
            Vertex neighbor = getOtherVertex(current, edge);
            if (neighbor != null && !visited.contains(neighbor)) {
                if (goUp && neighbor.getYPos() >= current.getYPos()
                        || !goUp && neighbor.getYPos() <= current.getYPos()) {
                    if (shortestPathPathDoorHelper(neighbor, goal, goUp, visited, path)) {
                        return true;
                    }
                }
            }
        }

        path.remove(current);
        visited.remove(current);
        return false;
    }

    Vertex getVertex(Vertex v) {
        List.Node<Vertex> current = vertices.getHead();
        Vertex ret = null;
        while (current != null) {
            Vertex vertex = current.getData();
            if (vertex.equals(v)) {
                ret = vertex;
            }
            current = current.getNext();
        }
        return ret;
    }

    boolean isReachAbleThroughDoor(Vertex start, Vertex goal) {
        if (start == null || goal == null) {
            return false;
        }

        Vertex[] keys = getAllKeys();
        Vertex[] doors = getAllDoors();

        sortVertices(keys);
        sortVertices(doors);

        for (Vertex key : keys) {
            if (isReachAble(start, key)) {
                for (Vertex door : doors) {
                    if (isReachAble(key, door)) {
                        if (isReachAble(door, goal)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void sortVertices(Vertex[] vertices) {
        for (int i = 0; i < vertices.length - 1; i++) {
            for (int j = 0; j < vertices.length - i - 1; j++) {
                if (vertices[j].toString().compareTo(vertices[j + 1].toString()) > 0) {
                    Vertex temp = vertices[j];
                    vertices[j] = vertices[j + 1];
                    vertices[j + 1] = temp;
                }
            }
        }
    }

    Vertex[] isReachAbleThroughDoorPath(Vertex start, Vertex goal) {
        if (start == null || goal == null) {
            return new Vertex[0];
        }

        List<Vertex> visited = new List<>();
        List<Vertex> path = new List<>();
        boolean found = isReachAblePathHelper(start, goal, visited, path);

        if (found) {
            return path.toArray(new Vertex[path.getSize()]);
        } else {
            return new Vertex[0];
        }

    }

    double shortestPathThroughDoor(Vertex start, Vertex goal) {
        if (start == null || goal == null) {
            return Double.POSITIVE_INFINITY;
        }
    
        double minDistance = Double.POSITIVE_INFINITY;
    
        Vertex[] keys = getAllKeys();
        Vertex[] doors = getAllDoors();
    
        for (Vertex key : keys) {
            double distanceToKey = shortestPathDistanceNoDoor(start, key);
            if (distanceToKey != Double.POSITIVE_INFINITY) {
                for (Vertex door : doors) {
                    double distanceToDoor = shortestPathDistanceNoDoor(key, door);
                    if (distanceToDoor != Double.POSITIVE_INFINITY) {
                        double distanceFromDoor = shortestPathDistanceDoor(door, goal, door.getYPos() < key.getYPos());
                        if (distanceFromDoor != Double.POSITIVE_INFINITY) {
                            double totalDistance = distanceToKey + distanceToDoor + distanceFromDoor;
                            minDistance = Math.min(minDistance, totalDistance);
                        }
                    }
                }
            }
        }
    
        return minDistance;
    }

    Vertex[] shortestPathThroughDoorPath(Vertex start, Vertex goal) {
        if (start == null || goal == null) {
            return new Vertex[0];
        }

        double minDistance = Double.POSITIVE_INFINITY;
        List<Vertex> shortestPath = new List<>();

        Vertex[] keys = getAllKeys();
        Vertex[] doors = getAllDoors();

        // sortVertices(keys);
        // sortVertices(doors);

        for (Vertex key : keys) {
            double distanceToKey = shortestPathDistanceNoDoor(start, key);
            if (distanceToKey != Double.POSITIVE_INFINITY) {
                for (Vertex door : doors) {
                    double distanceToDoor = shortestPathDistanceNoDoor(key, door);
                    if (distanceToDoor != Double.POSITIVE_INFINITY) {
                        boolean goUp = door.getYPos() < key.getYPos();
                        double distanceFromDoor = shortestPathDistanceDoor(door, goal, goUp);
                        if (distanceFromDoor != Double.POSITIVE_INFINITY) {
                            double totalDistance = distanceToKey + distanceToDoor + distanceFromDoor;
                            if (totalDistance < minDistance) {
                                minDistance = totalDistance;
                                shortestPath.clear();
                                shortestPath.addAll(constructPath(start, key));
                                shortestPath.addAll(constructPath(key, door));
                                shortestPath.addAll(constructPathFromDoor(door, goal, goUp));
                            }
                        }
                    }
                }
            }
        }

        return shortestPath.toArray(new Vertex[0]);
    }

    private List<Vertex> constructPath(Vertex start, Vertex end) {
        List<Vertex> path = new List<>();
        Vertex[] pathArray = shortestPathPathNoDoor(start, end);
        for (Vertex vertex : pathArray) {
            path.add(vertex);
        }
        return path;
    }

    private List<Vertex> constructPathFromDoor(Vertex start, Vertex end, boolean goUp) {
        List<Vertex> path = new List<>();
        Vertex[] pathArray = shortestPathPathDoor(start, end, goUp);
        for (Vertex vertex : pathArray) {
            path.add(vertex);
        }
        return path;
    }

    boolean canReachGoal(char targetGoal) {
        Vertex[] goals = getAllGoals();
        Vertex targetGoalVertex = null;

        for (Vertex goal : goals) {
            if (goal.getSymbol() == targetGoal) {
                targetGoalVertex = goal;
                break;
            }
        }

        if (targetGoalVertex == null) {
            return false;
        }

        Vertex start = this.start;
        if (isReachAble(start, targetGoalVertex)) {
            return true;
        }

        return isReachAbleThroughDoor(start, targetGoalVertex);
    }

    Vertex[] canReachGoalPath(char targetGoal) {
        Vertex[] goals = getAllGoals();
        Vertex targetGoalVertex = null;

        for (Vertex goal : goals) {
            if (goal.getSymbol() == targetGoal) {
                targetGoalVertex = goal;
                break;
            }
        }

        if (targetGoalVertex == null) {
            return new Vertex[0];
        }

        Vertex start = this.start;
        Vertex[] path = isReachAblePath(start, targetGoalVertex);
        if (path.length > 0) {
            return path;
        }

        path = isReachAbleThroughDoorPath(start, targetGoalVertex);
        return path;
    }

    double getRatio(Vertex goal) {
        if (goal == null) {
            return Double.POSITIVE_INFINITY;
        }

        boolean isGoalInMaze = false;
        Vertex[] vertices = getVertices();
        for (Vertex vertex : vertices) {
            if (vertex.equals(goal)) {
                isGoalInMaze = true;
                break;
            }
        }

        if (!isGoalInMaze) {
            return Double.POSITIVE_INFINITY;
        }

        int goalTreasure = (int) goal.getSymbol() * 100;
        double shortestDistance;

        Vertex start = this.start;
        Vertex[] pathNoDoor = shortestPathPathNoDoor(start, goal);
        if (pathNoDoor.length > 0) {
            shortestDistance = shortestPathDistanceNoDoor(start, goal);
        } else {
            shortestDistance = shortestPathThroughDoor(start, goal);
        }

        if (shortestDistance == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        return (double) goalTreasure / shortestDistance;
    }

    Vertex getRecommendedGoal() {
        Vertex[] goals = getAllGoals();
        Vertex recommendedGoal = null;
        double maxRatio = Double.NEGATIVE_INFINITY;

        for (Vertex goal : goals) {
            double ratio = getRatio(goal);
            if (ratio > maxRatio) {
                maxRatio = ratio;
                recommendedGoal = goal;
            }
        }

        return recommendedGoal;
    }

    double getRecommendedRatio() {
        Vertex recommendedGoal = getRecommendedGoal();
        if (recommendedGoal == null) {
            return Double.NEGATIVE_INFINITY;
        }
        return getRatio(recommendedGoal);
    }

    Vertex[] getRecommendedPath() {
        Vertex recommendedGoal = getRecommendedGoal();
        if (recommendedGoal == null) {
            return new Vertex[0];
        }

        Vertex start = this.start;
        Vertex[] pathNoDoor = shortestPathPathNoDoor(start, recommendedGoal);
        if (pathNoDoor.length > 0) {
            return pathNoDoor;
        } else {
            return shortestPathThroughDoorPath(start, recommendedGoal);
        }

    }

}
