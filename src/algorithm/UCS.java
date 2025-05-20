package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import object.Board;
import object.State;

public class UCS {
    private int nodesVisited;
    private long executionTime;
    private State initialState;
    private State goalState;
    
    public UCS(Board initialBoard) {
        this.initialState = new State(initialBoard);
        this.nodesVisited = 0;
        this.executionTime = 0;
        this.goalState = null;
    }
    
    public boolean execute() {
        long startTime = System.currentTimeMillis();
        
        // Priority queue sorted by cost (Uniform Cost Search uses g(n) only)
        PriorityQueue<State> openSet = new PriorityQueue<>(Comparator.comparingInt(State::getCost));
        Set<Board> closedSet = new HashSet<>();
        
        openSet.add(initialState);
        
        while (!openSet.isEmpty()) {
            // Get the state with the lowest cost
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