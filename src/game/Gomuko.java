package src.game;

import src.interfaces.Game;

public class Gomuko implements Game {
    private Player player1;
    private Player player2 = null;
    private String table = "table as string";

    public Gomuko (Player owner) { 
        this.player1 = owner;
    }

    public void disconnect(Player player) {
        if(player == player1)
         player1 = null;
        else player2 = null;
    }

    public Player getOwner(){
        return this.player1;
    }


    public String getTable() {
        return this.table;
    }

    @Override
    public void processResults() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processResults'");
    }

    @Override
    public void processPlay() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPlay'");
    }

    @Override
    public void join(Player player) {
        this.player2 = player;
    }
    @Override
    public String toString() {
        return "P1: %s, P2: %s"
                .formatted(this.player1, this.player2);
    }
}