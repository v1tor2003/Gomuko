package src.interfaces;

import src.game.GameState;
import src.game.Player;

public interface Game {
    void join(Player player);
    void start();
    void finish();
    boolean isFull();
    GameState processPlay(GameState gameState, Player player, int row, int col);
}
