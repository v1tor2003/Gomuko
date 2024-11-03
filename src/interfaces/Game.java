package src.interfaces;

import src.game.Player;

public interface Game {
    void join (Player player);
    void processPlay (Player player, int row, int col);
}
