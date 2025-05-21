package IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;

import object.Board;
import object.Car;

public class InputParser {

public static Board parse(String filename) throws IOException, InputMismatchException, FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader(filename));

    String[] dims = reader.readLine().trim().split("\\s+");
    int expectedRows = Integer.parseInt(dims[0]);
    int expectedCols = Integer.parseInt(dims[1]);

    int nonPrimaryCars = Integer.parseInt(reader.readLine().trim());

    // Baca semua sisa baris sebagai isi grid
    String line;
    List<String> lineList = new ArrayList<>();
    int maxCols = 0;
    while ((line = reader.readLine()) != null) {
        lineList.add(line);
        maxCols = Math.max(maxCols, line.length());
    }

    int rows = lineList.size();
    int cols = maxCols;

    // Buat grid
    char[][] grid = new char[rows][cols];
    for (int i = 0; i < rows; i++) {
        String l = lineList.get(i);
        for (int j = 0; j < cols; j++) {
            grid[i][j] = (j < l.length()) ? l.charAt(j) : ' ';
        }
    }
           
    Board board = new Board(rows, cols);
    int exitRow = -1, exitCol = -1;
    int exitCount = 0;

    Set<Character> visited = new HashSet<>();
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            char c = grid[i][j];

            if (c == 'K') {
                exitRow = i;
                exitCol = j;
                exitCount++;
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
        if (exitCount > 1) {
            System.out.println("pintu keluar K tidak valid, ditemukan: " + exitCount);
            break;
        }
    }
    board.setExit(exitRow, exitCol);
    board.validatePrimaryCarExitAlignment();
    reader.close();
    return board;
}

}

