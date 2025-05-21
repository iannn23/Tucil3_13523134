package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

import object.Board;
import object.Car;
import object.State;

public class GreedyBestFirst {
    private int nodesVisited;
    private long executionTime;
    private State initialState;
    private State goalState;
    private Function<Board, Integer> heuristicFunction;

    public GreedyBestFirst(Board initialBoard, Function<Board, Integer> heuristicFunction) {
        this.initialState = new State(initialBoard);
        this.nodesVisited = 0;
        this.executionTime = 0;
        this.goalState = null;
        this.heuristicFunction = heuristicFunction;
        
        // Calculate heuristic for initial state
        initialState.setHeuristic(heuristicFunction.apply(initialBoard));
    }

    public GreedyBestFirst(Board initialBoard) {
        this(initialBoard, GreedyBestFirst::blockingCarsHeuristic);
    }
    
    public boolean execute() {
        long startTime = System.currentTimeMillis();
        
        // Priority queue sorted by heuristic value only for Greedy Best First Search
        PriorityQueue<State> openSet = new PriorityQueue<>(Comparator.comparingInt(State::getHeuristic));
        Set<Board> closedSet = new HashSet<>();
        
        openSet.add(initialState);
        
        while (!openSet.isEmpty()) {
            // Get the state with the lowest heuristic value
            State current = openSet.poll();
            nodesVisited++;
            
            // Check if goal state is reached
            if (current.isGoal()) {
                goalState = current;
                executionTime = System.currentTimeMillis() - startTime;
                return true;
            }
            
            // Add current board to closed set
            closedSet.add(current.getBoard());
            
            // Generate and process next states
            for (State nextState : current.generateNextStates()) {
                // Skip if we've already visited this board configuration
                if (closedSet.contains(nextState.getBoard())) {
                    continue;
                }
                
                // Calculate heuristic for next state
                nextState.setHeuristic(heuristicFunction.apply(nextState.getBoard()));
                
                // Add to open set
                openSet.add(nextState);
            }
        }
        
        executionTime = System.currentTimeMillis() - startTime;
        return false;
    }

    public List<State> getSolutionPath() {
        if (goalState == null) {
            return Collections.emptyList();
        }
        
        List<State> path = new ArrayList<>();
        State current = goalState;
        
        // Trace back from goal to initial state
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        
        // Reverse to get path from initial to goal
        Collections.reverse(path);
        return path;
    }
    
    public static int blockingCarsHeuristic(Board board) {
        Car primaryCar = board.getPrimaryCar();
        if (primaryCar == null) {
            return Integer.MAX_VALUE; // No primary car found
        }
        
        int blockingCars = 0;
        char[][] grid = board.getGrid();
        
        // Determine exit direction
        int exitRow = board.getExitRow();
        int exitCol = board.getExitCol();
        
        if (primaryCar.isHorizontal()) {
            // Primary car is horizontal, check cars blocking to the right or left
            int startRow = primaryCar.getRow();
            
            if (exitCol > primaryCar.getCol()) {
                // Exit is to the right
                int startCol = primaryCar.getCol() + primaryCar.getLength();
                for (int c = startCol; c <= exitCol; c++) {
                    if (c < board.getCols() && grid[startRow][c] != '.' && grid[startRow][c] != 'K') {
                        blockingCars++;
                    }
                }
            } else {
                // Exit is to the left
                for (int c = primaryCar.getCol() - 1; c >= exitCol; c--) {
                    if (c >= 0 && grid[startRow][c] != '.' && grid[startRow][c] != 'K') {
                        blockingCars++;
                    }
                }
            }
        } else {
            // Primary car is vertical, check cars blocking up or down
            int startCol = primaryCar.getCol();
            
            if (exitRow > primaryCar.getRow()) {
                // Exit is below
                int startRow = primaryCar.getRow() + primaryCar.getLength();
                for (int r = startRow; r <= exitRow; r++) {
                    if (r < board.getRows() && grid[r][startCol] != '.' && grid[r][startCol] != 'K') {
                        blockingCars++;
                    }
                }
            } else {
                // Exit is above
                for (int r = primaryCar.getRow() - 1; r >= exitRow; r--) {
                    if (r >= 0 && grid[r][startCol] != '.' && grid[r][startCol] != 'K') {
                        blockingCars++;
                    }
                }
            }
        }
        
        return blockingCars;
    }
    
    public static int distanceAndBlockingHeuristic(Board board) {
        Car primaryCar = board.getPrimaryCar();
        if (primaryCar == null) {
            return Integer.MAX_VALUE; // No primary car found
        }
        
        int blockingCars = blockingCarsHeuristic(board);
        int distance = 0;
        
        // Calculate distance from primary car to exit
        if (primaryCar.isHorizontal()) {
            int exitCol = board.getExitCol();
            if (exitCol > primaryCar.getCol()) {
                // Exit is to the right
                distance = exitCol - (primaryCar.getCol() + primaryCar.getLength() - 1);
            } else {
                // Exit is to the left
                distance = primaryCar.getCol() - exitCol;
            }
        } else {
            int exitRow = board.getExitRow();
            if (exitRow > primaryCar.getRow()) {
                // Exit is below
                distance = exitRow - (primaryCar.getRow() + primaryCar.getLength() - 1);
            } else {
                // Exit is above
                distance = primaryCar.getRow() - exitRow;
            }
        }
        
        // Combine distance and blocking cars (weighted)
        return distance + 2 * blockingCars;
    }
    
    public int getNodesVisited() {
        return nodesVisited;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }
    
    public int getNumberOfMoves() {
        return goalState != null ? goalState.getCost() : -1;
    }
}