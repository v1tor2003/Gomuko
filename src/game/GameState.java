package src.game;

import java.io.Serializable;

public record GameState (
    Player owner, 
    Player participant,
    String gameTable,
    int lobbyId
) implements Serializable {}
