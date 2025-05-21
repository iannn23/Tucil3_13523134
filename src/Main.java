import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

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

        System.out.println("Masukkan path file input (tanpa .txt): ");
        String filename = scanner.nextLine().trim();
        String filepath = "../test/" + filename + ".txt";

        System.out.println("Pilih algoritma (UCS / GBFS / AStar): ");
        String algo = scanner.nextLine().trim().toLowerCase();

        Function<Board, Integer> heuristicFunction = GreedyBestFirst::blockingCarsHeuristic;
        if (algo.equals("gbfs") || algo.equals("astar")) {
            System.out.println("Pilih heuristic (blocking / distance):");
            String heuristic = scanner.nextLine().trim().toLowerCase();
            System.out.println("\n");

            if (heuristic.equals("distance")) {
                heuristicFunction = GreedyBestFirst::distanceAndBlockingHeuristic;
            } else if (!heuristic.equals("blocking")) {
                System.out.println("Heuristic tidak dikenali. Menggunakan heuristic default (blocking).");
            }
        }

        try {
            Board board = InputParser.parse(filepath);
            List<State> path = null;
            int nodesVisited = 0;
            long executionTime = 0;
            System.out.println("Papan Awal:");
            board.printBoard();
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
                    GreedyBestFirst gbfs = new GreedyBestFirst(board, heuristicFunction);
                    if (gbfs.execute()) {
                        path = gbfs.getSolutionPath();
                        nodesVisited = gbfs.getNodesVisited();
                        executionTime = gbfs.getExecutionTime();
                    }
                    break;
                case "astar":
                    AStar astar = new AStar(board, heuristicFunction);
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
                OutputFile.saveSolution(path, nodesVisited, executionTime, "../test/solution_" + filename + ".txt");
                System.out.println("Solusi ditulis ke file: solution_"+ filename + ".txt");
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
