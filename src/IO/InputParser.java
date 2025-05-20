package IO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import object.Board;
import object.Car;

public class InputParser {

    public static Board parse(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String[] dims = br.readLine().trim().split("\\s+");
        int rows = Integer.parseInt(dims[0]);
        int nonPrimaryCars = Integer.parseInt(br.readLine().trim());

        String[] lines = new String[rows];
        int maxCols = 0;
        for (int i = 0; i < rows; i++) {
            lines[i] = br.readLine();
            if (lines[i] != null && lines[i].length() > maxCols) {
                maxCols = lines[i].length();
            }
        }
        int cols = maxCols;

        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = lines[i];
            for (int j = 0; j < cols; j++) {
                grid[i][j] = (j < line.length()) ? line.charAt(j) : ' ';
            }
        }

        Board board = new Board(rows, cols);
        int exitRow = -1, exitCol = -1;

        Set<Character> visited = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = grid[i][j];

                if (c == 'K') {
                    exitRow = i;
                    exitCol = j;
                    continue;
                }

                if (c == '.' || c == ' ' || visited.contains(c)) continue;

                visited.add(c);
                boolean horizontal = (j + 1 < cols && grid[i][j + 1] == c);
                int length = 1;

                if (horizontal) {
                    while (j + length < cols && grid[i][j + length] == c) length++;
                } else {
                    while (i + length < rows && grid[i + length][j] == c) length++;
                }

                boolean isPrimary = (c == 'P');
                Car car = new Car(c, i, j, length, horizontal, isPrimary);
                board.addCar(car);
            }
        }

        board.setExit(exitRow, exitCol);
        br.close();
        return board;
    }
}
