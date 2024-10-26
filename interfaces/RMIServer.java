package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import classes.Player;

public interface RMIServer extends Remote {
    int createGame (Player owner) throws RemoteException;
    void join (int lobbyId, Player player) throws RemoteException;
    String getGameState(int lobbyId) throws RemoteException;
 }
