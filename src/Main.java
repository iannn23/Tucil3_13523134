// import IO.InputParser;
// import java.io.*;
// import java.util.*;
// import object.*;

// public class Main {
//     public static void main(String[] args) throws IOException {
//         Scanner scanner = new Scanner(System.in);

//         System.out.print("Masukkan nama file input(tanpa .txt): ");
//         String filename = scanner.nextLine();

//         System.out.print("Pilih algoritma (ucs, gbfs, astar): ");
//         String algo = scanner.nextLine().toLowerCase();

//         Board board = InputParser.parse("../test/" + filename + ".txt");

//         // Solver solver;
//         // switch (algo) {
//         //     case "ucs" -> solver = new UCS();
//         //     // case "gbfs" -> solver = new GreedyBestFirst();
//         //     case "astar" -> solver = new AStar();
//         //     default -> throw new IllegalArgumentException("Algoritma tidak dikenali.");
//         // }

//         // long start = System.currentTimeMillis();
//         // List<Move> solution = solver.solve(board);
//         // long end = System.currentTimeMillis();

//         System.out.println("Papan Awal:");
//         board.printBoard();
//         // System.out.println(board.getPrimaryPiece());
//         System.out.println("Exit is at: " + board.getExitRow() + "," + board.getExitCol());


//     //     if (solution == null) {
//     //         System.out.println("Tidak ditemukan solusi.");
//     //     } else {
//     //         System.out.println("Papan Awal:");
//     //         board.printBoard();

//     //         Board currentBoard = board;
//     //         for (int i = 0; i < solution.size(); i++) {
//     //             Move move = solution.get(i);
//     //             System.out.println("Gerakan " + (i + 1) + ": " + move);
//     //             currentBoard = currentBoard.applyMove(move);
//     //             currentBoard.printBoard();
//     //         }
//     //         System.out.println("Waktu eksekusi: " + (end - start) + "ms");
//         //     }
//         }
//     }

// import IO.InputParser;
// import IO.OutputFile;
// import algorithm.AStar;
// import algorithm.GreedyBestFirst;
// import algorithm.UCS;
// import java.util.List;
// import java.util.Scanner;
// import object.Board;
// import object.State;
import java.util.List;
import java.util.Scanner;

import IO.InputParser;
import IO.OutputFile;
import algorithm.AStar;
import algorithm.GreedyBestFirst;
import algorithm.UCS;
import object.Board;
import object.State;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Masukkan path file input (misal: test/input01.txt):");
        String filepath = scanner.nextLine().trim();

        System.out.println("Pilih algoritma (ucs / gbfs / astar):");
        String algo = scanner.nextLine().trim().toLowerCase();

        try {
            Board board = InputParser.parse(filepath);
            List<State> path = null;
            int nodesVisited = 0;
            long executionTime = 0;
            System.out.println("Papan Awal:");
            board.printBoard();
            // OutputFile.printSolution(path, nodesVisited, executionTime);;
            // // System.out.println(board.getPrimaryPiece());
            System.out.println("Exit is at: " + board.getExitRow() + "," + board.getExitCol());


            switch (algo) {
                case "ucs":
                    UCS ucs = new UCS(board);
                    if (ucs.execute()) {
                        path = ucs.getSolutionPath();
                        nodesVisited = ucs.getNodesVisited();
                        executionTime = ucs.getExecutionTime();
                    }
                    break;
                case "gbfs":
                    GreedyBestFirst gbfs = new GreedyBestFirst(board);
                    if (gbfs.execute()) {
                        path = gbfs.getSolutionPath();
                        nodesVisited = gbfs.getNodesVisited();
                        executionTime = gbfs.getExecutionTime();
                    }
                    break;
                case "astar":
                    AStar astar = new AStar(board);
                    if (astar.execute()) {
                        path = astar.getSolutionPath();
                        nodesVisited = astar.getNodesVisited();
                        executionTime = astar.getExecutionTime();
                    }
                    break;
                default:
                    System.out.println("Algoritma tidak dikenali.");
                    return;
            }

            if (path != null && !path.isEmpty()) {
                OutputFile.printSolution(path, nodesVisited, executionTime);
                OutputFile.saveSolution(path, nodesVisited, executionTime, "output_solution.txt");
                System.out.println("Solusi ditulis ke file: output_solution.txt");
            } else {
                System.out.println("Tidak ditemukan solusi.");
            }

        } catch (Exception e) {
            System.err.println("Terjadi kesalahan saat menjalankan program:");
            e.printStackTrace();
        }

        scanner.close();
    }
}
