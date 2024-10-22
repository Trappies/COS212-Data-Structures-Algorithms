import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class MazeGenerator {
    private char[][] arr;
    private Random random;

    public int nextInt(int min, int max){
        return random.nextInt(max-min) + min;
        //return random.nextInt(min, max);
    }

    public MazeGenerator(int seed, int numDoors, int numGoals, int numTraps, int size) {
        size = size < 10 ? 10 : size;
        numGoals = numGoals <= 0 ? 2 : numGoals;
        numGoals = numGoals > 10 ? 5 : numGoals;
        numDoors = numDoors <= 0 ? 1 : numDoors;
        numTraps = numTraps < 0 ? 0 : numTraps;
        random = new Random(seed);

        arr = new char[size][size];
        int[][] doorCoordinates = new int[numDoors][2];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = '-';
            }
        }
        int prevX = 1;
        for (int i = 0; i < numDoors; i++) {
            int offSet = size / 8;
            int center = size / 2;
            if(prevX+1 >= size-2){
                break;
            }
            doorCoordinates[i][1] = nextInt(prevX + 1, size - 2);
            doorCoordinates[i][0] = nextInt(-1 * offSet, offSet) + center;
            prevX = doorCoordinates[i][1];
            arr[doorCoordinates[i][0]][doorCoordinates[i][1]] = 'D';
        }

        for (int i = 0; i < size; i++) {
            if (i == 0 || i == size - 1) {
                for (int j = 0; j < size; j++) {
                    arr[i][j] = '#';
                }
            } else {
                arr[i][0] = '#';
                arr[i][size - 1] = '#';
            }
        }

        prevX = 0;
        int prevY = size / 2;

        for (int i = 0; i < doorCoordinates.length; i++) {
            if (doorCoordinates[i] == null) {
                break;
            }

            int currentY = prevY;
            int nextX = doorCoordinates[i][1];
            int nextY = doorCoordinates[i][0];

            for (int currentX = prevX; currentX <= nextX; currentX++) {
                arr[currentY][currentX] = '#';
                if (currentY < nextY) {
                    currentY++;
                } else if (currentY > nextY) {
                    currentY--;
                }
            }

            arr[currentY][nextX++] = '#';
            prevX = nextX;
            prevY = nextY;
        }

        int currentY = prevY;
        int nextX = size;
        int nextY = size / 2;

        for (int currentX = prevX; currentX < nextX; currentX++) {
            arr[currentY][currentX] = '#';
            if (currentY < nextY) {
                currentY++;
            } else if (currentY > nextY) {
                currentY--;
            }
        }

        for (int i = 0; i < numDoors; i++) {
            if (doorCoordinates[i] == null) {
                break;
            }
            arr[doorCoordinates[i][0]][doorCoordinates[i][1]] = 'D';
        }

        int[][] keyPositions = new int[numDoors][2];
        int numKeys = 0;

        int prevKeyY = 0;
        for (int i = 0; i < numDoors; i++) {
            int attempts = 0;
            int x = nextInt(1, size - 2);
            int y = nextInt(prevKeyY, size - 2);
            while (arr[y][x] != '-' && Math.abs(prevKeyY - y) <= 1) {
                attempts++;

                if (attempts > 5) {
                    break;
                } else {
                    x = nextInt(1, size - 2);
                    y = nextInt(prevKeyY, size - 2);
                }
            }

            if (arr[y][x] == '-') {
                arr[y][x] = 'K';
                keyPositions[numKeys][1] = x;
                keyPositions[numKeys][0] = y;
                numKeys++;
                prevKeyY = y;
                continue;
            }
        }

        int prevTrapY = 1;
        for (int i = 0; i < numTraps; i++) {
            int attempts = 0;
            int x = nextInt(1, size - 2);
            int y = nextInt(prevTrapY, size - 2);
            while (arr[y][x] != '-' && Math.abs(prevKeyY - y) <= 1) {
                attempts++;

                if (attempts > 5) {
                    break;
                } else {
                    x = nextInt(1, size - 2);
                    y = nextInt(prevTrapY, size - 2);
                }
            }

            if (arr[y][x] == '-') {
                arr[y][x] = 'T';
                prevTrapY = y;
                for (int j = 1; j < arr[y].length; j++) {
                    arr[y][j] = arr[y][j] == '-' ? ' ' : arr[y][j];
                }
                continue;
            }
        }

        for (int i = 0; i < numGoals; i++) {
            int attempts = 0;
            int x = nextInt(1, size - 2);
            int y = nextInt(1, size - 2);
            while (arr[y][x] != '-') {
                attempts++;

                if (attempts > 5) {
                    break;
                } else {
                    x = nextInt(1, size - 2);
                    y = nextInt(1, size - 2);
                }
            }

            if (arr[y][x] == '-') {
                arr[y][x] = Integer.toString(i).charAt(0);
                for(int j=1; j < size-1; j++){
                    arr[y][j] = arr[y][j] == '-' ? ' ' : arr[y][j];
                }
                continue;
            }
        }
        startX = nextInt(1, arr[arr.length - 1].length - 1);
        startY = arr.length - 1;
        arr[startY][startX] = 'S';

        for (int i = 0; i < arr.length; i++) {
            arr[i][startX] = arr[i][startX] == '-' ? ' ' : arr[i][startX];
        }

        for (int i = 0; i < doorCoordinates.length; i++) {
            if (doorCoordinates[i] == null) {
                break;
            }
            int doorX = doorCoordinates[i][1];
            for (int j = 0; j < arr.length; j++) {
                arr[j][doorX] = arr[j][doorX] == '-' ? ' ' : arr[j][doorX];
            }
        }

        for (int i = 0; i < keyPositions.length; i++) {
            if (keyPositions[i] == null) {
                break;
            }
            int keyY = keyPositions[i][0];
            for (int j = 0; j < arr[keyY].length; j++) {
                arr[keyY][j] = arr[keyY][j] == '-' ? ' ' : arr[keyY][j];
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[j][i] == '-') {
                    arr[j][i] = '#';
                }
            }
        }
    }

    int startX;
    int startY;

    public String toString() {
        String res = "";
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                res += arr[i][j];
            }
            res += '\n';
        }
        return res;
    }

    public String toMarkDown() {
        String result = "| |";
        for (int i = 0; i < arr.length; i++) {
            result += i + "|";
        }
        result += "|\n|-|-";
        for (int i = 0; i < arr.length; i++) {
            result += "|-";
        }
        result += "|\n";
        for (int y = 0; y < arr.length; y++) {
            result += "|" + y;
            for (int x = 0; x < arr[y].length; x++) {
                if (arr[y][x] == '#') {
                    result += "|\\" + arr[y][x];
                } else {
                    result += "|" + arr[y][x];
                }
            }
            result += "|\n";
        }

        return result;
    }

    public static String toMarkDown(String fileName){
        String lines = "";
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                lines += data + "\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String[] arr = lines.split("\n");
        String result = "| |";
        for (int i = 0; i < arr[0].length(); i++) {
            result += i + "|";
        }
        result += "|\n|-|-";
        for (int i = 0; i < arr[0].length(); i++) {
            result += "|-";
        }
        result += "|\n";
        for(int i=0; i < arr.length; i++){
            result += "|" + i;
            for (int j = 0; j < arr[i].length(); j++) {
                result += "|" + arr[i].charAt(j);
            }
            result += "|\n";
        }
        return result;
    }
}
