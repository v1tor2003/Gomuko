package interfaces;

import classes.Player;

public interface Game {
    void join (Player player);
    void processResults ();
    void processPlay ();
}
