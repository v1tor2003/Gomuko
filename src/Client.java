package src;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import src.game.Player;
import src.interfaces.GameServer;
import src.lib.Menu;

public class Client {
    public Client () {}

    public static void main (String ...args) 
    throws  RemoteException, 
            NotBoundException, 
            AlreadyBoundException, 
            InterruptedException {
        final String serverName = "RMIServer";
        var server = getServerStub(serverName);
        var player = new Player(server);
        var menu = new Menu();

        menu.greet(new String []{
            "----\tBem-Vindo ao GOMUKO\t----",  
            "Para comecar, insira seu nome de jogador:"
        });

        player.setNick(menu.getPlayerName());
        player.setMenu(menu);

        menu.setOptions(new String[]{
            "----\t\tGOMUKO\t\t----",
            "Pressione 'L' para listar todas as salas de jogo.",
            "Pressione 'E' para entrar em uma sala de jogo.",
            "Pressione 'C' para criar uma nova sala de jogo.",
            "Pressione 'Q' para sair do jogo."
        });

        menu.clearScreen();
        char choice = Character.MIN_VALUE;
        while (choice != menu.getExitOption()) {
            choice = menu.getChoice();  
            handleChoice(
                choice, 
                player
            );
        }
    }

    private static GameServer getServerStub(String serverName) 
    throws  AccessException, 
            RemoteException, 
            NotBoundException {
        return (GameServer) java.rmi.registry.LocateRegistry.getRegistry().lookup(serverName);
    }
    
    private static void handleChoice(char choice, Player player) 
    throws RemoteException, 
           InterruptedException{
        switch(choice){
            case 'c' -> player.createGame();
            case 'l' -> player.listGames();
            case 'e' -> player.joinGame(player.askGameId());
            case 'q' -> player.quitGame();
            default -> System.out.println("Escolha uma opcao valida. (L, E, C, Q)");
        }
    }
}