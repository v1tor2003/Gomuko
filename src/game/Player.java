package src.game;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import src.interfaces.PlayerActions;
import src.lib.ClientImpl;

public class Player implements PlayerActions, Serializable {
    private String nick;
    private ClientImpl client;

    public Player (String nick, ClientImpl client) {
        this.nick = nick;
        this.client = client;
    }

    public Player (ClientImpl client) {
        this("", client);
    }

    public void setNickName(String nickName) {
        this.nick = nickName;
    }

    public ClientImpl getClient() {
        return this.client;
    }

    @Override
    public void joinGame(int lobbyId) {
        try {
            GameState res = this.client.getServerStub().join(lobbyId, this);
            if(res.lobbyId() != lobbyId) {
                System.out.println("Erro ao se juntar a sala %d.".formatted(lobbyId));
                return;
            }
            System.out.println("Voce se juntou a sala %d.".formatted(lobbyId));
            System.out.println("Partidade iniciada!"); 
            System.out.println("Eh a vez de %s, aguarde...".formatted(res.owner()));
            System.out.println(res.gameTable());
            
            synchronized (this.client) {
                this.client.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quitGame() {
       System.out.println("quit game");
    }

    @Override
    public void createGame() {
        try {
            int res = this.client.getServerStub().createGame(this);
            if(res == -1) {
                System.out.println("Erro ao criar sala. tente novamente.");
                return;
            } 

            System.out.println("Voce criou e se juntou a sala %d".formatted(res));
            synchronized (this.client) {
                System.out.println("Aguardando inicio da partida...");
                this.client.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'play'");
    }

    @Override
    public String toString(){
        return this.nick;
    }

    @Override
    public void listGames() {
        try {
            System.out.println("Salas ativas: \n" +
                            this.client.getServerStub().listGames());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, Method> getPlayerActions() throws NoSuchMethodException, SecurityException {
        Map<String, Method> methodMap = new HashMap<>();

        methodMap.put("c", this.getClass().getDeclaredMethod("createGame"));
        methodMap.put("e", this.getClass().getDeclaredMethod("joinGame", int.class));
        methodMap.put("q", this.getClass().getDeclaredMethod("quitGame"));
        methodMap.put("l", this.getClass().getDeclaredMethod("listGames"));
        
        return methodMap;
    }

    
}
