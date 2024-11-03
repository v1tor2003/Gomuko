package src.game;

import src.interfaces.Game;

public class Gomuko implements Game {
    private static final int BOARD_SIZE = 15;
    private static final int WIN_COUNT = 5;
    private Player player1; // X
    private Player player2 = null; // 0
    private GameState gameState;
    private Board gameBoard = null;

    public Gomuko (Player owner) { 
        this.player1 = owner;
        this.gameState = new GameState(0, true, false,"");
    }

    public Player getOwner(){
        return this.player1;
    }

    public Player getParticipant() {
        return this.player2;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void start() {
        this.gameBoard = new Board(BOARD_SIZE);
        this.gameState = new GameState(1, false, true, this.gameBoard.toString());
    }

    private char getPlayerSymbol(Player player){
        return player == player1 ? 'X' : 'O';
    }

    public void setTurn(){
        int newTurn = this.gameState.turn() == 1 ? 2 : 1;
        this.gameState = new GameState(newTurn, false, true, this.gameBoard.toString());
    }

    public Board getGameBoard() {
        return this.gameBoard;
    }

    @Override
    public void join(Player player) {
        this.player2 = player;
    }

    @Override
    public void processPlay(Player player, int row, int col) {
        char playerSymbol = getPlayerSymbol(player);
        this.gameBoard.setCell(row, col, playerSymbol);
        if(!checkWin(row, col, playerSymbol)) return;

        //should end game and shi
        System.out.println(player + "ganhou");
    }   

    public boolean checkWin(int row, int col, char playerSymbol){
        char[][] board = this.gameBoard.getInternalBoard();
        if(playerSymbol == this.gameBoard.getEmptySymbol()) return false;

        return     checkDirection(board, playerSymbol, row, col, 0, 1)   // Horizontal
                || checkDirection(board, playerSymbol, row, col, 1, 0)  // Vertical
                || checkDirection(board, playerSymbol, row, col, 1, 1)  // Diagonal (bottom-left to top-right)
                || checkDirection(board, playerSymbol, row, col, 1, -1); // Anti-diagonal (top-left to bottom-right)

    }

    private boolean checkDirection(char[][] board, char playerSymbol, int row, int col, int rowDelta, int colDelta){
        int count = 1; // count with curr piece/cell
        // positive way
        count += countConsecutive(board, playerSymbol, row, col, rowDelta, colDelta);
        // negative way
        count += countConsecutive(board, playerSymbol, row, col, -rowDelta, -colDelta);

        return count >= WIN_COUNT;
    }

    private int countConsecutive(char[][] board, char playerSymbol, int startRow, int startCol, int rowDelta, int colDelta) {
        int count = 0;
        int row = startRow + rowDelta;
        int col = startCol + colDelta;

        while (this.gameBoard.IsInsideBoard(row, col) && this.gameBoard.IsCurrSymbolPlayerSymbol(row, col, playerSymbol)) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    @Override
    public String toString() {
        String status = this.gameState.waiting() ? "Aguardando" : "Jogando";
        return "P1: %s, P2: %s, Status: %s"
                .formatted(this.player1, this.player2, status);
    }
}