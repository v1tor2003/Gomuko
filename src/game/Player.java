package src.game;

import java.io.Serializable;
import java.rmi.RemoteException;
import src.interfaces.GameServer;
import src.lib.Menu;

public class Player implements Serializable {
    private String nick;
    private GameServer server;
    private Menu menuRef;
    private Turn playerTurn;

    public Player (String nick, GameServer server) {
        this.nick = nick;
        this.server = server;
    }

    public Player (GameServer server) {
        this("", server);
    }

    public void setMenu(Menu ref){
        this.menuRef = ref;
    }

    public Menu getMenuRef() {
        return this.menuRef;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    
    public String getNick() {
        return this.nick;
    }

    private void waitAndPlay(GameState gameState) throws RemoteException, InterruptedException {
        int dotCounter = 0;
        while (gameState.waiting() || gameState.going()) {
            if(isPlayerTurn(gameState.turn())){
                int[] coordinates = this.askCoordinates(gameState.board(), gameState.gameId());
                gameState = this.server.play(gameState.gameId(), this, coordinates[0], coordinates[1]);
            }
            
            dotCounter += 1;
            this.printWaitingScreen(dotCounter, 1000);
            if(dotCounter == 3) dotCounter = 0;
            
            gameState = this.server.getGameState(gameState.gameId());
        }
    }

    public void createGame() throws RemoteException, InterruptedException {
        this.playerTurn = Turn.OWNER;
        GameState gameState = this.server.createGame(this);

        if(!isGameValid(gameState)) {
            System.out.println("Erro ao criar novo jogo! Tente novamente.");
            return;
        }

        this.waitAndPlay(gameState);

        gameState = this.server.getGameState(gameState.gameId());
        String results = this.processResults(gameState);
        System.out.println(results);
        this.exitRoom(gameState.gameId());
    }

    public void joinGame(int gameId) throws RemoteException, InterruptedException {
        this.playerTurn = Turn.PARTICIPANT;
        GameState gameState = this.server.connectGame(gameId, this);

        if(!isGameValid(gameState)) {
            System.out.println("Erro ao se conectar ao novo jogo %d! Tente novamente.".formatted(gameId));
            return;
        }

        this.waitAndPlay(gameState);
        gameState = this.server.getGameState(gameId);
        String results = this.processResults(gameState);
        System.out.println(results);
        this.exitRoom(gameState.gameId());
    }

    private boolean isPlayerTurn(Turn gameTurn) {
        return gameTurn == this.playerTurn;
    }

    private boolean isGameValid(GameState gameState) {
        return gameState != null;
    }

    private boolean isGameIdValid(int gameId) throws RemoteException{
        GameState gameState = this.server.getGameState(gameId);
        return isGameValid(gameState);
    }

    public void listGames() throws RemoteException {
        System.out.println("Salas de jogo ativas: ");
        String gamesStr = this.server.getGames();
        if(gamesStr.isBlank())
            System.out.println("Sem salas ativas no momento.");
        else
            System.out.println(this.server.getGames());   
    }

    private void exitRoom(int gameId) throws RemoteException, InterruptedException {
        if(this.playerTurn == Turn.OWNER)
            if(!isGameValid(this.server.closeGame(gameId))) 
                throw new Error("Erro ao fechar sala.");

        System.out.println("A sala de jogo foi fechada. Voltando ao menu inicial...");
        
        Thread.sleep(2000);
    }

    public void quitGame() {
        this.menuRef.close();
        System.out.println("Saindo...");
        System.exit(0);
    }

    private int[] askCoordinates(String board, int gameId) {
        System.out.println(board + "Sua vez! Informe as coodenadas de sua jogada. (l [1, 15], c [1, 15]):");
        return this.getPlayCoordinates(gameId);
    }

    private int[] getPlayCoordinates(int gameId) {
        while (true) {
            try {
                int row = this.menuRef.getIntInput() - 1;
                int col =  this.menuRef.getIntInput() - 1;

                if(!this.server.isValidPlay(gameId, row, col)) {
                    System.out.println("Informe coordenadas validas! [1, 15]");
                    continue;
                }

                return new int[] { row, col };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int askGameId() {
        while (true) {
            try {
                System.out.println("Informe o id sala de jogo:");
                int gameId = this.menuRef.getIntInput() - 1;
                
                if(this.isGameIdValid(gameId)) return gameId;

                System.out.println("Informe um id de sala valido! id: [1, %d]".formatted(this.server.getGamesCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String processResults(GameState gameState){
        this.menuRef.clearScreen();
        String str = "Jogo finalizado!\n";
        String res = this.isPlayerTurn(gameState.turn()) ? "ganhou" : "perdeu";
        
        return str += "Voce %s a partida!".formatted(res);
    }

    private void printWaitingScreen(int dotRepeatAmount, long waitingTime) throws InterruptedException{
        System.out.println("Aguarde sua vez" + ".".repeat(dotRepeatAmount));
        Thread.sleep(waitingTime); 
        this.menuRef.clearScreen();
    }

    @Override
    public String toString(){
        String role = playerTurn == Turn.OWNER ? "Dono" : "Participante";
        return this.nick + ", " + role;
    }
}
