import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze {
  private String[] map;

  public Maze(String filename) {
    try {
      Scanner scanner = new Scanner(new File(filename));
      int rows = Integer.parseInt(scanner.nextLine());
      map = new String[rows];
      fillMap(scanner, 0);
      scanner.close();
    } catch (FileNotFoundException e) {
      map = new String[0];
    }
  }

  private void fillMap(Scanner scanner, int index) {
    if (scanner.hasNextLine() && index < map.length) {
      map[index] = scanner.nextLine();
      fillMap(scanner, index + 1);
    }
  }

  public Maze(Maze other) {
    this.map = other.map.clone();
  }

  @Override
  public String toString() {
    if (map.length == 0) {
      return "Empty Map";
    } else {
      return toString(0);
    }
  }

  private String toString(int index) {
    if (index >= map.length) {
      return "";
    } else {
      return map[index] + "\n" + toString(index + 1);
    }
  }

  private boolean isValidCoordinate(int x, int y) {
    return map != null && y >= 0 && y < map.length && map[y] != null && x >= 0 && x < map[y].length()
        && map[y].charAt(x) != 'X';
  }

  public boolean validSolution(int startX, int startY, int goalX, int goalY, LinkedList path) {
    if (path.head == null || path.reversed().head == null) {
      return false;
    } else if (path.head.x != startX || path.head.y != startY || path.length() == 0 || path.reversed().head.x != goalX
        || path.reversed().head.y != goalY) {
      return false;
    }

    return validSolution(path.head, path.head.next);
  }

  private boolean validSolution(CoordinateNode current, CoordinateNode next) {
    if (next == null) {
      return true;
    } else if (Math.abs(current.x - next.x) + Math.abs(current.y - next.y) != 1 || !isValidCoordinate(next.x, next.y)
        || current == null || next == null) {
      return false;
    } else {
      return validSolution(next, next.next);
    }
  }

  private LinkedList solve(int x, int y, int goalX, int goalY, LinkedList path) {
    if (x == goalX && y == goalY) {
        if (path != null) {
            path.append(x, y);
            return path;
        } else {
            return new LinkedList();
        }
    } else if (!isValidCoordinate(x, y) || (path != null && path.contains(x, y))) {
        return new LinkedList();
    } else {
        LinkedList newPath = new LinkedList();
        if (path != null) {
            newPath.appendList(path);
        }
        newPath.append(x, y);

        LinkedList result = solve(x - 1, y, goalX, goalY, newPath);
        if (result.length() > 0) return result;

        result = solve(x, y - 1, goalX, goalY, newPath);
        if (result.length() > 0) return result;

        result = solve(x, y + 1, goalX, goalY, newPath);
        if (result.length() > 0) return result;

        result = solve(x + 1, y, goalX, goalY, newPath);
        if (result.length() > 0) return result;

        return new LinkedList();
    }
}


  public String solve(int x, int y, int goalX, int goalY) {
    LinkedList solution = solve(x, y, goalX, goalY, new LinkedList());
    if (solution.length() == 0 || !validSolution(x, y, goalX, goalY, solution)) {
      return "No valid solution exists";
    } else {
      return "Solution\n" + printMaze(solution, 0, 0, x, y, goalX, goalY) + "\n" + solution.toString();
    }
  }

  private String printMaze(LinkedList solution, int x, int y, int startX, int startY, int goalX, int goalY) {
    if (map == null || y >= map.length) {
      return "";
    } else if (x >= map[y].length()) {
      return "\n" + printMaze(solution, 0, y + 1, startX, startY, goalX, goalY);
    } else {
      char c = ' ';
      if (map[y] != null && x >= 0 && x < map[y].length()) {
        c = map[y].charAt(x);
      }
      if (x == startX && y == startY) {
        c = 'S';
      } else if (x == goalX && y == goalY) {
        c = 'E';
      } else if (solution != null && solution.contains(x, y)) {
        c = '@';
      }
      return c + printMaze(solution, x + 1, y, startX, startY, goalX, goalY);
    }
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