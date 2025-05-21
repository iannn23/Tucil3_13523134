package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

import object.Board;
import object.State;

public class AStar {
    private int nodesVisited;
    private long executionTime;
    private State initialState;
    private State goalState;
    private Function<Board, Integer> heuristicFunction;
    
    public AStar(Board initialBoard, Function<Board, Integer> heuristicFunction) {
        this.initialState = new State(initialBoard);
        this.nodesVisited = 0;
        this.executionTime = 0;
        this.goalState = null;
        this.heuristicFunction = heuristicFunction;
        
        initialState.setHeuristic(heuristicFunction.apply(initialBoard));
    }
    
    public AStar(Board initialBoard) {
        this(initialBoard, GreedyBestFirst::blockingCarsHeuristic);
    }

    public boolean execute() {
        long startTime = System.currentTimeMillis();
        
        PriorityQueue<State> openSet = new PriorityQueue<>();
        Map<Board, Integer> bestCostSoFar = new HashMap<>();
        Set<Board> closedSet = new HashSet<>();
        
        openSet.add(initialState);
        bestCostSoFar.put(initialState.getBoard(), 0);
        
        while (!openSet.isEmpty()) {
            State current = openSet.poll();
            nodesVisited++;
            
            // Check if goal state is reached
            if (current.isGoal()) {
                goalState = current;
                executionTime = System.currentTimeMillis() - startTime;
                return true;
            }
            
            // Skip if we've processed this state with a better cost
            Integer bestCost = bestCostSoFar.get(current.getBoard());
            if (bestCost != null && current.getCost() > bestCost) {
                continue;
            }
            
            // Add current board to closed set
            closedSet.add(current.getBoard());
            
            // Generate and process next states
            for (State nextState : current.generateNextStates()) {
                Board nextBoard = nextState.getBoard();
                
                // Skip if we've already visited this board configuration with a better cost
                if (closedSet.contains(nextBoard)) {
                    continue;
                }
                
                nextState.setHeuristic(heuristicFunction.apply(nextBoard));
                
                // Check if we found a better path to this board
                Integer currentBestCost = bestCostSoFar.get(nextBoard);
                if (currentBestCost == null || nextState.getCost() < currentBestCost) {
                    bestCostSoFar.put(nextBoard, nextState.getCost());
                    openSet.add(nextState);
                }
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