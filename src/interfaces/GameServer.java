package src.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import src.game.GameState;
import src.game.Player;

public interface GameServer extends Remote {
    GameState getGameState(int gameId) throws RemoteException;
    GameState createGame(Player owner) throws RemoteException;
    GameState closeGame(int gameId) throws RemoteException;
    GameState connectGame(int gameId, Player participant) throws RemoteException;
    GameState play(int gameId, Player player, int row, int col) throws RemoteException;
    String getGames() throws RemoteException;
    int getGamesCount() throws RemoteException;  
    boolean isValidPlay(int gameId, int row, int col) throws RemoteException;
}
