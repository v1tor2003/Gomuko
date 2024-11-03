package src;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import src.game.GameState;
import src.game.Gomuko;
import src.game.Player;
import src.interfaces.GameServer;

public class Server implements GameServer {
    private List<Gomuko> games;

    public Server(String serverName, int port) throws RemoteException, AlreadyBoundException {
        this.games = new ArrayList<>();
        GameServer stub = (GameServer) UnicastRemoteObject.exportObject(this, port);
        java.rmi.registry.LocateRegistry.getRegistry().rebind(serverName, stub);
    }
   
    public Server(String serverName) throws RemoteException, AlreadyBoundException {
        this(serverName, 0);
    }

    public static void main(String... args) throws RemoteException, AlreadyBoundException {
        new Server("RMIServer");
        System.out.println("Sever is up and running");
    }

    private Gomuko getGameById(int gameId) {
        return this.games.get(gameId);
    }

    private boolean IsValidGameId(int gameId) {
        return gameId >= 0 || gameId < games.size();
    }

    @Override
    public GameState getGameState(int gameId) throws RemoteException {
        System.out.println(gameId + this.getGameById(gameId).getGameState().toString());
        
        if(!IsValidGameId(gameId)) return null;

        return this.getGameById(gameId).getGameState();
    }

    @Override
    public int createGame(Player owner) throws RemoteException {
        this.games.add(new Gomuko(owner));

        return this.games.size() - 1;
    }

    @Override
    public GameState connectGame(int gameId, Player participant) throws RemoteException {
        if(!IsValidGameId(gameId)) return null;

        Gomuko game = this.getGameById(gameId);
        if(game.getParticipant() != null) return null; // check if game is full
        game.join(participant);
        game.start();

        return game.getGameState();
    }

    @Override
    public GameState play(int gameId, Player player, int row, int col) throws RemoteException {
        if(!IsValidGameId(gameId)) return null;
        Gomuko game = this.getGameById(gameId);
        game.processPlay(player, row, col);
        
        game.setTurn();
        return game.getGameState();
    }

    @Override
    public boolean IsValidPlay(int gameId, int row, int col) throws RemoteException {
        var board = this.getGameById(gameId).getGameBoard();
        return board.IsInsideBoard(row, col) && board.IsCellEmpty(row, col);
    }

    @Override
    public String getGames() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.games.size(); i++) 
            sb.append(String.format("Id: %d, %s \n",  i + 1, this.games.get(i)));
   
        return sb.toString();
    }
}
