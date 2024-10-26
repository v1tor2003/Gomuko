package classes;

import interfaces.PlayerActions;
import java.io.Serializable;

public class Player implements PlayerActions, Serializable {
    private String nick;

    public Player (String nick) {
        this.nick = nick;
    }

    @Override
    public void join(int lobbyId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'play'");
    }

    @Override
    public String toString () {
        return this.nick;
    }
    
}
