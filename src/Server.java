package src;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import src.game.GameState;
import src.game.Gomoku;
import src.game.Player;
import src.interfaces.GameLogger;
import src.interfaces.GameServer;
import src.lib.LoggerIpml;

public class Server implements GameServer {
    private List<Gomoku> games;
    private GameLogger gameLogger;

    public Server(String serverName, int port) throws RemoteException, AlreadyBoundException {
        this.games = new ArrayList<>();
        this.gameLogger = new LoggerIpml();
        GameServer stub = (GameServer) UnicastRemoteObject.exportObject(this, port);
        java.rmi.registry.LocateRegistry.getRegistry().rebind(serverName, stub);
    }
   
    public Server(String serverName) throws RemoteException, AlreadyBoundException {
        this(serverName, 0);
    }

    public static void main(String... args) throws RemoteException, AlreadyBoundException {
        final String serverName = "RMIServer";
        new Server(serverName);
        System.out.println("Sever is up and running at rmi//:localhost/%s".formatted(serverName));
    }

    private Gomoku getGameById(int gameId) {
        return this.games.get(gameId);
    }

    private boolean isValidGameId(int gameId) {
        return gameId >= 0 && gameId < games.size();
    }

    @Override
    public GameState getGameState(int gameId) throws RemoteException {
        if(!isValidGameId(gameId)) return null;

        return this.getGameById(gameId).getGameState();
    }

    @Override
    public GameState createGame(Player owner) throws RemoteException {
        var newGame = new Gomoku(this.games.size(), owner);
        this.games.add(newGame);
        GameState newGameState = newGame.getGameState();
        
        String[] info = {
            "New game created with id: %d for %s".formatted(this.games.size(), owner.getNick()),
            "\t\t There are " + this.games.size() + " active games."
        };

        this.gameLogger.logInfo(info);

        return newGameState;
    }
    
    @Override
    public GameState closeGame(int gameId) throws RemoteException {
        if(!isValidGameId(gameId)) return null;
        
        Gomoku game = this.getGameById(gameId);
        game.finish();
        this.games.remove(game);

        this.gameLogger.logInfo("(id: %d): game room has been closed".formatted(gameId));
        return game.getGameState();
    }

    @Override
    public GameState connectGame(int gameId, Player participant) throws RemoteException {
        if(!isValidGameId(gameId)) return null;

        Gomoku game = this.getGameById(gameId);
        if(game.isFull()) return null; 
        
        game.join(participant);
        game.start();

        String logInfo = "(id: %d): %s joined %s's game room".formatted(gameId, participant.getNick(), game.getOwner().getNick());
        this.gameLogger.logInfo(logInfo);

        return game.getGameState();
    }

    @Override
    public GameState play(int gameId, Player player, int row, int col) throws RemoteException {
        if(!isValidGameId(gameId)) return null;

        Gomoku game = this.getGameById(gameId);
        GameState newGameState = game.processPlay(game.getGameState(), player, row, col);
        game.setGameState(newGameState);

        if(newGameState.going()) game.updateTurn();

        GameState gameState = game.getGameState();

        String[] info = {
            "(id: %d): %s played".formatted(gameId, player.getNick()),
            gameState.going() ? "\t\t Game is being played" : "\t\t Game is finished",
            gameState.going() ? "\t\t Next player turn is the " + gameState.turn().toString() : "\t\t No next turn."
        };

        this.gameLogger.logInfo(info);

        return gameState;
    }

    @Override
    public boolean isValidPlay(int gameId, int row, int col) throws RemoteException {
        var board = this.getGameById(gameId).getGameBoard();
        return board.isInsideBoard(row, col) && board.isCellEmpty(row, col);
    }

    @Override
    public String getGames() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.games.size(); i++) 
            sb.append(String.format("Id: %d, %s \n",  i + 1, this.games.get(i)));
   
        return sb.toString();
    }

    @Override
    public int getGamesCount() throws RemoteException {
        return this.games.size();
    }
}
