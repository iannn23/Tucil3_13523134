package object;

import java.util.List;
import java.util.Objects;


public class State implements Comparable<State> {
    public Board board;             // Current board configuration
    public State parent;            // Parent state
    public Board.BoardMove move;    // Move that led to this state
    public int cost;                // Cost to reach this state (number of moves)
    public int heuristic;           // Heuristic value

    public State(Board board) {
        this.board = board;
        this.parent = null;
        this.move = null;
        this.cost = 0;
        this.heuristic = 0;
    }
    
    public State(Board board, State parent, Board.BoardMove move) {
        this.board = board;
        this.parent = parent;
        this.move = move;
        this.cost = parent.cost + 1; // Increment cost by 1 for each move
        this.heuristic = 0;          // To be calculated later
    }
    private int getNewRowFromDirection(Car car, String direction) {
    return switch (direction) {
        case "up" -> car.getRow() - 1;
        case "down" -> car.getRow() + 1;
        default -> car.getRow(); // horizontal move
    };
}

private int getNewColFromDirection(Car car, String direction) {
    return switch (direction) {
        case "left" -> car.getCol() - 1;
        case "right" -> car.getCol() + 1;
        default -> car.getCol(); // vertical move
    };
}

    // public List<State> generateNextStates() {
    //     List<Board.BoardMove> possibleMoves = board.generateMoves();
    //     return possibleMoves.stream()
    //     //         .map(move -> new State(move.getBoard(), this, move))
    //     //         .toList(); debug
    //     .peek(move -> {
    //     // System.out.println("→ Menambahkan gerakan: " + move.getCarId() + " ke " + move.getDirection());
    //     move.getBoard().printBoard(); // pastikan tidak ada board null
    // })
    // .map(move -> new State(move.getBoard(), this, move))
    // .toList();
    // }
    public List<State> generateNextStates() {
    List<Board.BoardMove> possibleMoves = board.generateMoves();

    return possibleMoves.stream()
        .filter(move -> {
            // Validasi ekstra apakah move ini memang benar-benar valid
            Car car = board.getCars().get(move.getCarId());
            int newRow = car.isHorizontal() ? car.getRow() : getNewRowFromDirection(car, move.getDirection());
            int newCol = car.isHorizontal() ? getNewColFromDirection(car, move.getDirection()) : car.getCol();
            boolean valid = board.isValidMove(car, newRow, newCol);
            if (!valid) {
                // System.out.println("❌ Gerakan INVALID ditemukan di State.generateNextStates() → " + move);
                // board.printBoard();
            }
            return valid;
        })
        .map(move -> new State(move.getBoard(), this, move))
        .toList();
}

    public boolean isGoal() {
        return board.isSolved();
    }
    
    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }
    
    public int getFValue() {
        return cost + heuristic;
    }
    
    /**
     * Compare states based on their f(n) values
     */
    @Override
    public int compareTo(State other) {
        // Compare based on f(n) = g(n) + h(n)
        int fValueDiff = this.getFValue() - other.getFValue();
        if (fValueDiff != 0) {
            return fValueDiff;
        }
        
        // If f values are equal, prefer lower h values (more informed)
        return this.heuristic - other.heuristic;
    }
    
    // Getters
    public Board getBoard() {
        return board;
    }
    
    public State getParent() {
        return parent;
    }
    
    public Board.BoardMove getMove() {
        return move;
    }
    
    public int getCost() {
        return cost;
    }
    
    public int getHeuristic() {
        return heuristic;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        State other = (State) obj;
        return Objects.equals(board, other.board);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
    
    @Override
    public String toString() {
        return "State [cost=" + cost + ", heuristic=" + heuristic + ", f=" + getFValue() + "]";
    }
}