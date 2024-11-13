package src.game;

import java.io.Serializable;

public record GameState (
    int gameId,
    Turn turn,
    boolean waiting,
    boolean going,
    String board
) implements Serializable {}
