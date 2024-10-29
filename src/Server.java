package src;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import src.game.GameState;
import src.game.Gomuko;
import src.game.Player;
import src.interfaces.RMIServer;

public class Server implements RMIServer {
    private final static int port = 0;
    private List<Gomuko> games;

    public Server () {
        this.games = new ArrayList<>();
    }
  
    public static void main(String ...args) {
        try {
            Server server = new Server();
            RMIServer stub = (RMIServer) UnicastRemoteObject.exportObject(server, port);     
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("RMIServer", stub);    

            System.out.println("Server is up and running.");
        } catch (Exception e) {
            System.err.println("Server Exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int createGame(Player owner) throws RemoteException {
        this.games.add(new Gomuko(owner));
        return this.games.size();
    }

    @Override
    public GameState join(int lobbyId, Player player) throws RemoteException {
        Gomuko game = this.games.get(lobbyId - 1);
        game.join(player);
        // should unlock the owner
        return new GameState(
            game.getOwner(),
            player,
            game.getTable(),
            lobbyId
        );
    }

    @Override
    public String getGameState(int lobbyId) throws RemoteException {
        // should be the table
        return this.games.get(lobbyId).toString();
    }

    @Override
    public String listGames() throws RemoteException {
        return this.games.toString();
    }
}
