package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    public int rows;           
    public int cols;           
    public char[][] grid;   
    public Map<Character, Car> cars;
    public int exitRow;        
    public int exitCol;        
    
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        this.cars = new HashMap<>();
        
        // Initialize grid with empty cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = '.';
            }
        }
    }
    
    public Board copy() {
        Board newBoard = new Board(rows, cols);
        newBoard.exitRow = this.exitRow;
        newBoard.exitCol = this.exitCol;
        
        // Copy grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newBoard.grid[r][c] = this.grid[r][c];
            }
        }
        
        // Copy cars
        for (Car car : this.cars.values()) {
            newBoard.addCar(car);
        }
        
        return newBoard;
    }
    
    public void addCar(Car car) {
        cars.put(car.getId(), car);
        
        // Update grid with car positions
        int[][] cells = car.getOccupiedCells();
        for (int[] cell : cells) {
            int r = cell[0];
            int c = cell[1];
            if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] != 'K') {
                grid[r][c] = car.getId();
            }
        }
    }
    
    public void setExit(int row, int col) {
        this.exitRow = row;
        this.exitCol = col;
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            grid[row][col] = 'K'; // K represents the exit
        }
    }
    
    public Board moveCar(char carId, int newRow, int newCol) {
        Car car = cars.get(carId);
        if (car == null) {
            return null; // Car not found
        }

        // debug cetak tujuan gerak
    // System.out.println("Mencoba memindahkan mobil " + carId + " ke posisi (" + newRow + "," + newCol + ")");
        
        // Check if the move is valid
        if (!isValidMove(car, newRow, newCol)) {
        //     System.out.println(">> Gerakan TIDAK VALID untuk mobil " + carId + " ke (" + newRow + "," + newCol + ")");
        // this.printBoard(); // debug tampilkan board sebelum gerakan
            return null;
        }
        
        // Create a new board with the updated car position
        Board newBoard = this.copy();
        
        // Remove old car positions
        int[][] oldCells = car.getOccupiedCells();
        // for (int[] cell : oldCells) {
        //     int r = cell[0];
        //     int c = cell[1];
        //     if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] == carId) {
        //         newBoard.grid[r][c] = '.';
        //     }
        // }
        for (int[] cell : oldCells) {
            int r = cell[0];
            int c = cell[1];
            if (r >= 0 && r < rows && c >= 0 && c < cols) {
            newBoard.grid[r][c] = '.';  // jangan dicek carId lagi, langsung kosongkan
            }
        }         
        
        // Add car with new position
        Car movedCar = car.move(newRow, newCol);
        newBoard.cars.put(carId, movedCar);
        
        // Update grid with new car positions
        int[][] newCells = movedCar.getOccupiedCells();
        for (int[] cell : newCells) {
            int r = cell[0];
            int c = cell[1];
            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                // Don't overwrite the exit marker
                if (!(r == exitRow && c == exitCol)) {
                    newBoard.grid[r][c] = carId;
                }
            }
        }
        
        return newBoard;
    }
    
    public boolean isValidMove(Car car, int newRow, int newCol) {
        // // Check if the car is moving along its orientation
        // if (car.isHorizontal() && newRow != car.getRow()) {
        //     return false; // Horizontal car must move horizontally
        // }
        // if (!car.isHorizontal() && newCol != car.getCol()) {
        //     return false; // Vertical car must move vertically
        // }
        
        // // Check if the new position is within bounds and cells are empty
        // int[][] newCells = new int[car.getLength()][2];
        // for (int i = 0; i < car.getLength(); i++) {
        //     if (car.isHorizontal()) {
        //         newCells[i][0] = newRow;
        //         newCells[i][1] = newCol + i;
        //     } else {
        //         newCells[i][0] = newRow + i;
        //         newCells[i][1] = newCol;
        //     }
            
        //     int r = newCells[i][0];
        //     int c = newCells[i][1];
            
        //     // Check if the cell is within bounds
        //     if (r < 0 || r >= rows || c < 0 || c >= cols) {
        //         return false;
        //     }
            
        //     // Check if the cell is occupied by another car
        //     if (grid[r][c] != '.' && grid[r][c] != car.getId() && grid[r][c] != 'K') {
        //         return false;
        //     }
        // Buat salinan grid tanpa mobil yang sedang dipindah
    char[][] tempGrid = new char[rows][cols];
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            tempGrid[r][c] = grid[r][c];
        }
    }

    // Hapus posisi mobil yang sedang diuji
    for (int[] cell : car.getOccupiedCells()) {
        int r = cell[0];
        int c = cell[1];
        if (r >= 0 && r < rows && c >= 0 && c < cols && tempGrid[r][c] == car.getId()) {
            tempGrid[r][c] = '.';  // kosongkan
        }
    }

    // Cek apakah posisi barunya valid
    for (int i = 0; i < car.getLength(); i++) {
        int r = car.isHorizontal() ? newRow : newRow + i;
        int c = car.isHorizontal() ? newCol + i : newCol;

        if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
        if (tempGrid[r][c] != '.' && tempGrid[r][c] != 'K') return false;
    }
    return true;
}
    
    public List<BoardMove> generateMoves() {
        List<BoardMove> possibleMoves = new ArrayList<>();
        
        for (Car car : cars.values()) {
            int row = car.getRow();
            int col = car.getCol();
            
            if (car.isHorizontal()) {
                // Try moving left
                for (int newCol = col - 1; newCol >= 0; newCol--) {
                    Board newBoard = moveCar(car.getId(), row, newCol);
                    if (newBoard != null) {
                        possibleMoves.add(new BoardMove(newBoard, car.getId(), "left"));
                        break;
                    }
                    
                }
                
                // Try moving right
                for (int newCol = col + 1; newCol <= cols - car.getLength(); newCol++) {
                    Board newBoard = moveCar(car.getId(), row, newCol);
                    if (newBoard != null) {
                        possibleMoves.add(new BoardMove(newBoard, car.getId(), "right"));
                        break;
                    }
                }
            } else {
                // Try moving up
                for (int newRow = row - 1; newRow >= 0; newRow--) {
                    Board newBoard = moveCar(car.getId(), newRow, col);
                    if (newBoard != null) {
                        possibleMoves.add(new BoardMove(newBoard, car.getId(), "up"));
                        break;
                    }
                }
                
                // Try moving down
                for (int newRow = row + 1; newRow <= rows - car.getLength(); newRow++) {
                    Board newBoard = moveCar(car.getId(), newRow, col);
                    if (newBoard != null) {
                        possibleMoves.add(new BoardMove(newBoard, car.getId(), "down"));
                        break;
                    }
                }
            }
            if (car.isPrimary() && car.isHorizontal() && exitRow == car.getRow()) {
                int carRight = car.getCol() + car.getLength() - 1;
                if (exitCol > carRight) {
                    boolean pathClear = true;
                for (int c = carRight + 1; c <= exitCol; c++) {
                    char ch = grid[exitRow][c];
                if (ch != '.' && ch != 'K') {
                    pathClear = false;
                    break;
            }
        }
        if (pathClear) {
            Board newBoard = moveCar(car.getId(), car.getRow(), exitCol - car.getLength() + 1);
            if (newBoard != null) {
                possibleMoves.add(new BoardMove(newBoard, car.getId(), "right-to-exit"));
            }
        }
    }
}
        }
        
        return possibleMoves;
    }
    
    public boolean isSolved() {
        Car primaryCar = null;
        for (Car car : cars.values()) {
            if (car.isPrimary()) {
                primaryCar = car;
                break;
            }
        }
        
        if (primaryCar == null) {
            return false;
        }
        int carRow = primaryCar.getRow();
        int carCol = primaryCar.getCol();
        int carLength = primaryCar.getLength();
        
        // For horizontal primary car, check if it can reach the exit
        if (primaryCar.isHorizontal()) {
            // Exit must be on the same row as the primary car
            if (carRow != exitRow) {
                return false;
            }
            
            int carLeft = carCol;
            int carRight = carCol + carLength - 1;
            
            // Selesaikan jika ujung kanan berada tepat di exit (ke kanan)
            if (carRight == exitCol) return true;

        // Selesaikan jika ujung kiri berada tepat di exit (ke kiri)
            if (carLeft == exitCol) return true;
            // If exit is on the right edge
            if (exitCol == cols - 1) {
                // Check if the path to the exit is clear
                for (int c = carRight + 1; c < cols; c++) {
                    if (grid[exitRow][c] != '.' && grid[exitRow][c] != 'K') {
                        return false;
                    }
                }
                return carRight == exitCol;
            }
            // If exit is on the left edge
            else if (exitCol == 0) {
                // Check if the path to the exit is clear
                for (int c = primaryCar.getCol() - 1; c >= 0; c--) {
                    if (grid[exitRow][c] != '.' && grid[exitRow][c] != 'K') {
                        return false;
                    }
                }
                return carLeft == exitCol;
            }
        }
        // For vertical primary car, check if it can reach the exit
        else {
            // Exit must be on the same column as the primary car
            if (exitCol != primaryCar.getCol()) {
                return false;
            }
            
            int carBottomEdge = primaryCar.getRow() + primaryCar.getLength() - 1;
            
            // If exit is on the bottom edge
            if (exitRow == rows - 1) {
                // Check if the path to the exit is clear
                for (int r = carBottomEdge + 1; r < rows; r++) {
                    if (grid[r][exitCol] != '.' && grid[r][exitCol] != 'K') {
                        return false;
                    }
                }
                return true;
            }
            // If exit is on the top edge
            else if (exitRow == 0) {
                // Check if the path to the exit is clear
                for (int r = primaryCar.getRow() - 1; r >= 0; r--) {
                    if (grid[r][exitCol] != '.' && grid[r][exitCol] != 'K') {
                        return false;
                    }
                }
                return true;
            }
        }
        
        return false;
    }

    public Car getPrimaryCar() {
        for (Car car : cars.values()) {
            if (car.isPrimary()) {
                return car;
            }
        }
        return null;
    }
    
    public static class BoardMove {
        private Board board;
        private char carId;
        private String direction;
        
        public BoardMove(Board board, char carId, String direction) {
            this.board = board;
            this.carId = carId;
            this.direction = direction;
        }
        
        public Board getBoard() {
            return board;
        }
        
        public char getCarId() {
            return carId;
        }
        
        public String getDirection() {
            return direction;
        }
        
        @Override
        public String toString() {
            return carId + "-" + direction;
        }
    }
    
    // Getters
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public char[][] getGrid() {
        return grid;
    }
    
    public Map<Character, Car> getCars() {
        return cars;
    }
    
    public int getExitRow() {
        return exitRow;
    }
    
    public int getExitCol() {
        return exitCol;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(grid[r][c]);
            }
            if (r < rows - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    Board other = (Board) obj;
    return this.cars.equals(other.cars);
}


@Override
public int hashCode() {
    return cars.hashCode();
}

    public void printBoard() {
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            System.out.print(grid[r][c]);
        }
        System.out.println();
    }
}
}