package classes;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import interfaces.RMIServer;

public class Server implements RMIServer {
    private final static int port = 0;
    private Gomuko gomuko = null;

    public Server () { }
  
    public static void main(String ...args) {
        try {
            Server server = new Server();
            RMIServer stub = (RMIServer) UnicastRemoteObject.exportObject(server, port);     
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("RMIServer", stub);    

            System.out.println("Server is ready.");
        } catch (Exception e) {
            System.err.println("Server Exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int createGame(Player owner) throws RemoteException {
        this.gomuko = new Gomuko(owner);
        return 1;
    }

    @Override
    public void join(int lobbyId, Player player) throws RemoteException {
        this.gomuko.join(player);
    }

    @Override
    public String getGameState(int lobbyId) throws RemoteException {
        return this.gomuko.toString();
    }

    
}

