package src.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import src.game.GameState;
import src.game.Player;

public interface RMIServer extends Remote {
    int createGame (Player owner) throws RemoteException;
    GameState join (int lobbyId, Player player) throws RemoteException;
    String getGameState(int lobbyId) throws RemoteException;
    String listGames() throws RemoteException;
 }
