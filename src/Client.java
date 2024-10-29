package src;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import src.game.Player;
import src.lib.ClientImpl;
import src.lib.Menu;

public class Client {
    public Client () {}

    public static void main (String ...args) throws RemoteException, NotBoundException, AlreadyBoundException {
        final String serverName = "RMIServer";
        var client = new ClientImpl(serverName);
        var player = new Player(client);
        var menu = new Menu();

        menu.greet(new String []{
            "----\tBem-Vindo ao GOMUKO\t----",  
            "Para comecar, insira seu nome de jogador:"
        });

        player.setNickName(menu.getPlayerName());
            
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
                menu, 
                choice, 
                player
            );
        }
    }
    
    private static void handleChoice(Menu menu, char choice, Player player) throws RemoteException, NotBoundException {
        try {
            Map<String, Method> playerActions = player.getPlayerActions(); 
            Method method = playerActions.get(Character.toString(choice));
            
            if (method == null) {
                System.out.println("Acao invalida. Tente novamente.");
                return;
            }

            // Check method parameter count and types
            Class<?>[] paramTypes = method.getParameterTypes();
            Object[] args;

            if (paramTypes.length == 0) {
                // No parameters
                args = new Object[0];
            } else if (paramTypes.length == 1 && paramTypes[0] == int.class) {
                // Expecting an int argument
                args = new Object[]{askLobbyId(menu)};
            } else {
                System.out.println("Metodo nao suportado ou argumentos incorretos.");
                return;
            }

            // Invoke the method
            method.invoke(player, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static int askLobbyId(Menu menu) {
        System.out.print("Insira o id da sala de jogo: ");
        while (true) {
            try {
                int lobbyId = menu.getScanner().nextInt();
                menu.getScanner().nextLine();
                return lobbyId;
            } catch (Exception e) {
                System.out.println("Entrada invalida. Por favor, insira um numero.");
                menu.getScanner().nextLine(); 
            }
        }
    }
}