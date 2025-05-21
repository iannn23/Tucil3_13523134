package IO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import object.Board;
import object.State;

/**
 * Formats and outputs the solution to the Rush Hour puzzle.
 */
public class OutputFile {
    
    // ANSI color codes for console output
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    
    /**
     * Print the solution path to the console with colored output
     * 
     * @param path         Solution path
     * @param nodesVisited Number of nodes visited during search
     * @param time         Execution time in milliseconds
     */
    public static void printSolution(List<State> path, int nodesVisited, long time) {
        if (path.isEmpty()) {
            System.out.println("No solution found.");
            return;
        }
        
        System.out.println("Solution found!");
        System.out.println("Number of moves: " + (path.size() - 1));
        System.out.println("Nodes visited: " + nodesVisited);
        System.out.println("Execution time: " + time + " ms");
        System.out.println();
        
        // Print initial state
        System.out.println("Papan Awal");
        printBoardColored(path.get(0).getBoard(), null);
        
        // Print each step
        for (int i = 1; i < path.size(); i++) {
            State state = path.get(i);
            System.out.println("Gerakan " + i + ": " + state.getMove().toString());
            printBoardColored(state.getBoard(), state.getMove().getCarId());
        }
    }
    
    /**
     * Print a board with color highlighting
     * 
     * @param board        Board to print
     * @param movedCarId   ID of the car that was moved (null for initial state)
     */
    private static void printBoardColored(Board board, Character movedCarId) {
        char[][] grid = board.getGrid();
        
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                char cell = grid[r][c];
                
                if (cell == 'P') {
                    // Primary car in red
                    System.out.print(RED + cell + RESET);
                } else if (cell == 'K') {
                    // Exit in green
                    System.out.print(GREEN + cell + RESET);
                } else if (movedCarId != null && cell == movedCarId) {
                    // Moved car in yellow
                    System.out.print(YELLOW + cell + RESET);
                } else {
                    // Other cells
                    System.out.print(cell);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Save the solution to a file
     * 
     * @param path         Solution path
     * @param nodesVisited Number of nodes visited during search
     * @param time         Execution time in milliseconds
     * @param filePath     Path to save the solution
     * @throws IOException If there's an error writing to the file
     */
    public static void saveSolution(List<State> path, int nodesVisited, long time, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            if (path.isEmpty()) {
                writer.println("No solution found.");
                return;
            }
            
            writer.println("Solution found!");
            writer.println("Number of moves: " + (path.size() - 1));
            writer.println("Nodes visited: " + nodesVisited);
            writer.println("Execution time: " + time + " ms");
            writer.println();
            
            // Print initial state
            writer.println("Papan Awal");
            writer.println(path.get(0).getBoard().toString());
            writer.println();
            
            // Print each step
            for (int i = 1; i < path.size(); i++) {
                State state = path.get(i);
                writer.println("Gerakan " + i + ": " + state.getMove().toString());
                writer.println(state.getBoard().toString());
                writer.println();
            }
        }
    }
}