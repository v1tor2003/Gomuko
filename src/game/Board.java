package src.game;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Board {
    private char[][] board;
    private final static char emptySymbol = ' '; 

    public Board (int size) {
        this.board = new char[size][size];
        this.fillBoard(emptySymbol);
    }

    public char[][] getInternalBoard(){
        return this.board;
    }

    public void setCell(int row, int col, char val) {
        this.board[row][col] = val;
    }

    public char getEmptySymbol() {
        return emptySymbol;
    }

    private void fillBoard(char val) {
        for(char[] row : this.board)
            Arrays.fill(row, val);
    }

    public boolean placeMove(int row, int col, char val) {
        if(!IsValidMove(row, col)) return false;
        setCell(row, col, val);
        return true;
    }

    private boolean IsValidMove(int row, int col) {
        return IsInsideBoard(row, col) && IsCellEmpty(row, col);
    }

    public boolean IsInsideBoard(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board.length;
    }

    public boolean IsCurrSymbolPlayerSymbol(int row, int col, char playerSymbol) {
        return board[row][col] == playerSymbol;
    }

    public boolean IsCellEmpty(int row, int col) {
        return board[row][col] == emptySymbol;
    }

    private StringBuilder appendColumns(StringBuilder sb){
        sb.append("   ");
        IntStream.range(0, this.board.length)
                 .mapToObj(i -> i < 9 ? String.format("   %d", i + 1) : String.format("  %d", i + 1))
                 .forEach(sb::append);
        sb.append("\n");

        return sb;
    }

    private StringBuilder appendRows(StringBuilder sb) {
        IntStream.range(0, this.board.length).forEach(i -> {
            sb.append(String.format("%2d ", i + 1));
    
            IntStream.range(0, this.board.length)
                    .mapToObj(j -> String.format("| %c ", this.board[i][j]))
                    .forEach(sb::append);
    
            sb.append("|\n");
    
            sb.append("   ");
            IntStream.range(0, this.board.length).forEach(j -> sb.append("----"));
            sb.append("-\n");
        });

        return sb;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb = appendColumns(sb);
        sb = appendRows(sb);

        return sb.toString();
    }
}
