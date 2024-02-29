import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze {
    private String[] map;

    public Maze(String filename) {
        try {
            this.map = mazeReader(new Scanner(new File(filename)));
        }
        catch (FileNotFoundException e) {
            this.map = new String[0];
        }
    }

    private String[] mazeReader(Scanner sc) {
        if (!sc.hasNextLine()) {
            return new String[0];
        }
        int nRows = Integer.parseInt(sc.nextLine());
        String[] maze = new String[nRows];
        mazeLinesReader(sc, maze, 0);
        return maze;
    }

    private void mazeLinesReader(Scanner sc, String[] maze, int row) {
        if (row >= maze.length || !sc.hasNextLine()) {
            return;
        }
        maze[row] = sc.nextLine();
        mazeLinesReader(sc, maze, row + 1);
    }

    public Maze(Maze other) {
        this.map = recArrCopy(other.map, 0);
    }

    private String[] recArrCopy(String[] arr, int i) {
        if (i >= arr.length) {
            return new String[0];
        }

        String[] cp = recArrCopy(arr, i + 1);
        String[] newArr = new String[cp.length + 1];
        copyArrElements(arr, newArr, cp, i);
        newArr[cp.length] = arr[i];
        return newArr;
    }

    private void copyArrElements(String[] start, String[] end, String[] cp, int i) {
        if (i >= cp.length) {
            return;
        }

        end[i] = start[i];
        copyArrElements(start, end, cp, i + 1);
    }

    @Override
    public String toString() {
        if (map.length == 0) {
            return "Empty Map";
        }
        else
        {
            return recToString(this.map, 0);
        }
    }

    private String recToString(String[] arr, int i) {
        if (i == arr.length - 1) {
            return arr[i];
        }
        else
        {
            return arr[i] + "\n" + recToString(arr, i + 1);
        }
    }

    public boolean validSolution(int startX, int startY, int goalX, int goalY, LinkedList path) {
        if (path.head == null || path.head.x != startX || path.head.y != startY ||
            findTailRecursive(path.head).x != goalX || findTailRecursive(path.head).y != goalY) {
            return false;
        }
        boolean[][] visited = new boolean[map.length][map[0].length()];
        return isValidPath(startX, startY, goalX, goalY, path.head.next, path.head, visited);
    }

    private boolean isValidPath(int startX, int startY, int goalX, int goalY, CoordinateNode node, CoordinateNode prevNode, boolean[][] visited) {
        if (node == null) {
            return prevNode.x == goalX && prevNode.y == goalY;
        }
        if (node.x < 0 || node.y < 0 || node.x >= map.length ||
            node.y >= map[0].length() || map[node.x].charAt(node.y) == 'X') {
            return false;
        }
        if (Math.abs(node.x - prevNode.x) + Math.abs(node.y - prevNode.y) != 1) {
            return false;
        }
        if (visited[node.x][node.y]) {
            return false;
        }
        visited[node.x][node.y] = true;
        return isValidPath(startX, startY, goalX, goalY, node.next, node, visited);
    }

    private CoordinateNode findTailRecursive(CoordinateNode node) {
        if (node == null || node.next == null) {
            return node;
        }
        return findTailRecursive(node.next);
    }

    private boolean isVisited(CoordinateNode node, CoordinateNode pathNode) {
        if (pathNode == null) {
            return false;
        }
        if (node.x == pathNode.x && node.y == pathNode.y) {
            return true;
        }
        return isVisited(node, pathNode.next);
    }

  public String solve(int x, int y, int goalX, int goalY) {
    LinkedList path = new LinkedList();
    boolean[][] visited = new boolean[map.length][map[0].length()]; 
    if (validSolution(x, y, goalX, goalY)) {
      if (findPath(x, y, goalX, goalY, path, visited)) {
        markStartAndEnd(x, y, goalX, goalY);
        markSolutionPath(x, y, path.head, goalX, goalY);
        return "Solution\n" + mapToString(map, 0) + "\n" + path.toString();
      }
    }
    return "No valid solution exists";  
  }

  private boolean validSolution(int x, int y, int goalX, int goalY) {
    if (isOutOfBounds(x, y) || isOutOfBounds(goalX, goalY)) {
      return false; 
    }
    if (isBlocked(x, y) || isBlocked(goalX, goalY)) {
      return false;
    }
    if (x == goalX && y == goalY) {
      return false;
    }
    if (isDirectPathBlocked(x, y, goalX, goalY)) {
      return false;
    }
    return true;
  }
  
  private boolean isDirectPathBlocked(int x1, int y1, int x2, int y2) {
    if (x1 == x2) {
      for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
        if (isBlocked(x1, y)) {
          return true;
        }
      }
    } 
    else if (y1 == y2) {
      for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
        if (isBlocked(x, y1)) {
          return true;
        }
      }
    }
  
    return false;
  
  }
  
  private void markStartAndEnd(int startX, int startY, int goalX, int goalY) {
    map[startX] = updateChar(map[startX], startY, 'S');
    map[goalX] = updateChar(map[goalX], goalY, 'E');
  }

  private boolean findPath(int x, int y, int goalX, int goalY, LinkedList path, boolean[][] visited) {
    if (x == goalX && y == goalY) {
      return true;
    }
    if (isOutOfBounds(x, y) || isVisited(x, y, visited) || isBlocked(x, y)) {
      return false;
    }
    visited[x][y] = true;
    path.append(x, y);
    if (findPath(x, y - 1, goalX, goalY, path, visited) ||  
        findPath(x - 1, y, goalX, goalY, path, visited) ||
        findPath(x + 1, y, goalX, goalY, path, visited) ||
        findPath(x, y + 1, goalX, goalY, path, visited)) {
      return true;
    }
    path.removeLast();
    return false;
  }

  private void markSolutionPath(int startX, int startY, CoordinateNode node, int goalX, int goalY) {
    if (node == null) {
      return;
    }
    if ((node.x == goalX && node.y == goalY) || 
        (node.x == startX && node.y == startY)) { 
      markSolutionPath(startX, startY, node.next, goalX, goalY);
    } else {
      map[node.x] = updateChar(map[node.x], node.y, '@');
      markSolutionPath(startX, startY, node.next, goalX, goalY);
    }
  }

  private String updateChar(String str, int index, char newChar) {
    return str.substring(0, index) + newChar + str.substring(index + 1);
  }

  private boolean isOutOfBounds(int x, int y) {
    return x < 0 || y < 0 || x >= map.length || y >= map[0].length();
  }

  private boolean isVisited(int x, int y, boolean[][] visited) {
    return visited[x][y];
  }

  private boolean isBlocked(int x, int y) {
    return map[x].charAt(y) == 'X';
  }

  private String mapToString(String[] map, int i) {
    if (i == map.length) {
      return "";
    }
    return map[i] + "\n" + mapToString(map, i+1);
  }


      public LinkedList validStarts(int goalX, int goalY) {
        LinkedList validStartsList = new LinkedList();
        validStartsRecursive(0, 0, goalX, goalY, validStartsList);
        return validStartsList;
    }
    
    private void validStartsRecursive(int x, int y, int goalX, int goalY, LinkedList validStartsList) {
        if (x >= map.length) {
            return;
        }
        if (y >= map[x].length()) {
            validStartsRecursive(x + 1, 0, goalX, goalY, validStartsList);
            return;
        }
        if (hasValidPath(x, y, goalX, goalY, null)) {
            insertSorted(validStartsList, x, y);
        }
        validStartsRecursive(x, y + 1, goalX, goalY, validStartsList);
    }
    
    private boolean hasValidPath(int x, int y, int goalX, int goalY, CoordinateNode prevNode) {
        if (x == goalX && y == goalY) {
            return true;
        }
        if (x < 0 || y < 0 || x >= map.length || y >= map[x].length() || map[x].charAt(y) == 'X') {
            return false;
        }
        if (prevNode != null && Math.abs(x - prevNode.x) + Math.abs(y - prevNode.y) != 1) {
            return false;
        }
        return hasValidPath(x, y - 1, goalX, goalY, new CoordinateNode(x, y)) ||
                hasValidPath(x - 1, y, goalX, goalY, new CoordinateNode(x, y)) ||
                hasValidPath(x + 1, y, goalX, goalY, new CoordinateNode(x, y)) ||
                hasValidPath(x, y + 1, goalX, goalY, new CoordinateNode(x, y));
    }
    
    private void insertSorted(LinkedList list, int x, int y) {
        CoordinateNode newNode = new CoordinateNode(x, y);
        list.head = insertSorted(list.head, newNode);
    }
    
    private CoordinateNode insertSorted(CoordinateNode current, CoordinateNode newNode) {
        if (current == null || newNode.y < current.y || (newNode.y == current.y && newNode.x < current.x)) {
            newNode.next = current;
            return newNode;
        }
        current.next = insertSorted(current.next, newNode);
        return current;
    }

}