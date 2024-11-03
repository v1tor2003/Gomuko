package src.game;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import src.interfaces.GameServer;
import src.lib.Menu;

public class Player implements Serializable {
    private String nick;
    private GameServer server;
    private Menu menuRef;
    private int playerRole;

    public Player (String nick, GameServer server) {
        this.nick = nick;
        this.server = server;
    }

    public Player (GameServer server) {
        this("", server);
    }

    public Menu getMenuRef() {
        return this.menuRef;
    }

    public void setMenu(Menu ref){
        this.menuRef = ref;
    }

    public void setNickName(String nickName) {
        this.nick = nickName;
    }

    private void waitAndPlay(int gameId) throws RemoteException, InterruptedException {
        GameState state = this.server.getGameState(gameId);
        
        int count = 0;
        while (state.waiting() || state.going()) {
            if(IsPlayerTurn(state.turn())){
                System.out.println(state.board() + "Sua vez! Informe as coodenadas de sua jogada. (l [1, 15], c [1, 15]):");
                int[] coordinates = getPlayCoordinates(gameId);
                state = this.server.play(gameId, this, coordinates[0], coordinates[1]);
            }
            
            count += 1;
            System.out.println("Aguarde sua vez" + ".".repeat(count));
            Thread.sleep(1000); // waits ant tries again
            this.menuRef.clearScreen();
            if(count == 3) count = 0;
              
            state = this.server.getGameState(gameId);
        }
    }

    public void createGame() {
        try {
            this.playerRole = 1; // setting as lobby onwer
            int gameId = this.server.createGame(this);

            if(gameId == -1) {
                System.out.println("Erro ao criar novo jogo! Tente novamente.");
                return;
            }

            waitAndPlay(gameId);
            
            System.out.println("Jogo finalizado");
            System.out.println("print results");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinGame(int gameId) {
        try {
            this.playerRole = 2; // setting as lobby participant
            GameState state = this.server.connectGame(gameId, this);

            if(state == null) {
                System.out.println("Erro ao se conectar ao novo jogo %d! Tente novamente.".formatted(gameId));
                return;
            }

            waitAndPlay(gameId);

            // could run processResults or sum shit
            System.out.println("Jogo finalizado");
            System.out.println("print results");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listGames() {
        try {
            System.out.println("Salas de jogo ativas: ");
            String gamesStr = this.server.getGames();
            if(gamesStr.isBlank())
                System.out.println("Sem salas ativas no momento.");
            else
                System.out.println(this.server.getGames());   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean IsPlayerTurn(int turn) {
        return turn == this.playerRole;
    }

    private int[] getPlayCoordinates(int gameId) {
        while (true) {
            try {
                int row = this.menuRef.getIntInput() - 1;
                int col =  this.menuRef.getIntInput() - 1;

                if(!this.server.IsValidPlay(gameId, row, col)) {
                    System.out.println("Informe coordenadas validas! [1, 15]");
                    continue;
                }

                return new int[] { row, col };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void quitGame() {
        this.menuRef.close();
        System.out.println("Saindo...");
        System.exit(0);
    }

    public Map<String, Method> getPlayerActions() throws NoSuchMethodException, SecurityException {
        Map<String, Method> methodMap = new HashMap<>();

        methodMap.put("c", this.getClass().getDeclaredMethod("createGame"));
        methodMap.put("e", this.getClass().getDeclaredMethod("joinGame", int.class));
        methodMap.put("q", this.getClass().getDeclaredMethod("quitGame"));
        methodMap.put("l", this.getClass().getDeclaredMethod("listGames"));
        
        return methodMap;
    }
    @Override
    public String toString(){
        String role = playerRole == 1? "Dono" : "Participante";
        return this.nick + ", " + role;
    }
}
