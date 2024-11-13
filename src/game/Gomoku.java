package src.game;

import src.interfaces.Game;
import src.lib.LoggerIpml;

public class Gomoku implements Game {
    private static final int BOARD_SIZE = 15;
    private static final int WIN_COUNT = 5;
    private Player owner; // OX
    private Player participant = null; // O
    private GameState gameState;
    private Board gameBoard = null;

    public Gomoku (int gameId, Player owner) { 
        this.owner = owner;
        this.gameState = new GameState(
            gameId,
            Turn.NONE, 
            true, 
            false,
            ""
        );
    }

    public Player getOwner(){
        return this.owner;
    }

    public Player getParticipant() {
        return this.participant;
    }

    public GameState getGameState() {
        return this.gameState;
    }
    
    public Board getGameBoard() {
        return this.gameBoard;
    }

    private char getPlayerSymbol(){
        return this.gameState.turn() == Turn.OWNER ? 'X' : 'O';
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    @Override
    public boolean isFull() {
        return this.participant != null;
    }

    @Override
    public void start() {
        this.gameBoard = new Board(BOARD_SIZE);
        this.gameState = new GameState(
            gameState.gameId(),
            Turn.OWNER,
            false, 
            true, 
            this.gameBoard.toString()
        );
    }

    @Override
    public void finish() {
        this.gameState = new GameState(
            this.gameState.gameId(), 
            this.gameState.turn(), 
            false, 
            false, 
            this.gameBoard.toString()
        );
    }

    public void updateTurn(){
        Turn newTurn = this.gameState.turn() == Turn.OWNER ? Turn.PARTICIPANT : Turn.OWNER;
        this.gameState = new GameState(
            gameState.gameId(),
            newTurn, 
            false, 
            true, 
            this.gameBoard.toString()
        );
    }

    @Override
    public void join(Player player) {
        this.participant = player;
    }

    @Override
    public GameState processPlay(GameState gameState, Player player, int row, int col) {
        char playerSymbol = getPlayerSymbol();
        this.gameBoard.setCell(row, col, playerSymbol);
        if(!checkWin(row, col, playerSymbol)) return gameState;

        new LoggerIpml().logInfo("(id: %d) %s wins the match".formatted(gameState.gameId() ,player.getNick()));

        return new GameState(
            gameState.gameId(),
            gameState.turn(), 
            false, 
            false, 
            gameState.board()
        );
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

        while (this.gameBoard.isInsideBoard(row, col) && this.gameBoard.isCurrSymbolPlayerSymbol(row, col, playerSymbol)) {
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
                .formatted(this.owner, this.participant, status);
    }
}