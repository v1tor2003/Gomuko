package src.interfaces;

import src.game.Player;

public interface Game {
    void join (Player player);
    void processResults ();
    void processPlay ();
}
