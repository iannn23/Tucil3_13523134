package object;

public class Car {
    public char id;
    public int row, col;
    public int length;
    public boolean isHorizontal;
    public boolean isPrimary;

    public Car(char id, int row, int col, int length, boolean isHorizontal, boolean isPrimary) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
    }

    public Car(Car other){
        this.id = other.id;
        this.row = other.row;
        this.col = other.col;
        this.length = other.length;
        this.isHorizontal = other.isHorizontal;
        this.isPrimary = other.isPrimary;
    }

    public char getId() { 
        return id; 
    }

    public int getRow() { 
        return row; 
    }

    public int getCol() { 
        return col; 
    }

    public int getLength() { 
        return length; 
    }

    public boolean isHorizontal() { 
        return isHorizontal; 
    }

    public boolean isPrimary() { 
        return isPrimary; 
    }

    public void setRow(int row) { 
        this.row = row; 
    }

    public void setCol(int col) { 
        this.col = col; 
    }

    public int canMoveRight(char[][] board){
        int steps = 0;
        for (int i = col + length; i < board[0].length; i++){
            if (board[row][i] == '.' || board[row][i] == id){
                steps++;
            } else {
                break;
            }
        }
        return steps;
    }

    public int canMoveLeft(char[][] board){
        int steps = 0;
        for (int i = col - 1; i >= 0; i++){
            if (board[row][i] == '.' || board[row][i] == id){
                steps++;
            } else {
                break;
            }
        }
        return steps;
    }

    public int canMoveUp(char[][] board){
        int steps = 0;
        for (int i = row - 1; i >= 0; i--){
            if (board[i][col] == '.' || board[i][col] == id){
                steps++;
            } else {
                break;
            }
        }
        return steps;
    }

    public int canMoveDown(char[][] board){
        int steps = 0;
        for (int i = row + length; i < board.length; i++){
            if (board[i][col] == '.' || board[i][col] == id){
                steps++;
            } else {
                break;
            }
        }
        return steps;
    }

    public Car moveRight(int steps) {
        return new Car(id, row, col + steps, length, isHorizontal, isPrimary);
    }

    public Car moveLeft(int steps) {
        return new Car(id, row, col - steps,  length, isHorizontal, isPrimary);
    }

    public Car moveUp(int steps) {
        return new Car(id, row - steps, col,  length, isHorizontal, isPrimary);
    }

    public Car moveDown(int steps) {
        return new Car(id, row + steps, col, length, isHorizontal, isPrimary);
    }

    public Car clone() {
        return new Car(id, row, col, length, isHorizontal, isPrimary);
    }

    // @Override
    // public boolean equals(Object o) {
    //     if (!(o instanceof Position)) return false;
    //     return this.row == row && this.col == col;
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(row, col);
    // }

    public Car move(int newRow, int newCol) {
        return new Car(this.id, newRow, newCol, this.length, this.isHorizontal, this.isPrimary);
    }

    public int[][] getOccupiedCells() {
        int[][] cells = new int[length][2];
        
        for (int i = 0; i < length; i++) {
            if (isHorizontal) {
                cells[i][0] = row;
                cells[i][1] = col + i;
            } else {
                cells[i][0] = row + i;
                cells[i][1] = col;
            }
        }
        
        return cells;
    }


    @Override
    public String toString() {
        return "Car [id=" + id + ", position=(" + row + "," + col + "), length=" + length + 
               ", orientation=" + (isHorizontal ? "horizontal" : "vertical") + 
               ", isPrimary=" + isPrimary + "]";
    }
    
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    Car other = (Car) obj;
    return id == other.id &&
           row == other.row &&
           col == other.col &&
           length == other.length &&
           isHorizontal == other.isHorizontal &&
           isPrimary == other.isPrimary;
}

@Override
public int hashCode() {
    int result = Character.hashCode(id);
    result = 31 * result + row;
    result = 31 * result + col;
    result = 31 * result + length;
    result = 31 * result + Boolean.hashCode(isHorizontal);
    result = 31 * result + Boolean.hashCode(isPrimary);
    return result;
}



}