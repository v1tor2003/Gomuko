package src.game;

import java.io.Serializable;

public record GameState (
    int turn, // 1 = owner, 2 = participant (gotta use a enum)
    boolean waiting,
    boolean going,
    String board
) implements Serializable {}
