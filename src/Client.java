package src;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import src.game.Player;
import src.interfaces.GameServer;
import src.lib.Menu;

public class Client {
    public Client () {}

    public static void main (String ...args) throws RemoteException, NotBoundException, AlreadyBoundException {
        final String serverName = "RMIServer";
        var server = getServerStub(serverName);
        var player = new Player(server);
        var menu = new Menu();

        menu.greet(new String []{
            "----\tBem-Vindo ao GOMUKO\t----",  
            "Para comecar, insira seu nome de jogador:"
        });

        player.setNickName(menu.getPlayerName());
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

    private static GameServer getServerStub(String serverName) throws AccessException, RemoteException, NotBoundException {
        return (GameServer) java.rmi.registry.LocateRegistry.getRegistry().lookup(serverName);
    }
    
    private static void handleChoice(char choice, Player player) throws RemoteException, NotBoundException {
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
                System.out.println("Informe o id do jogo:");
                args = new Object[]{player.getMenuRef().getIntInput() - 1};
            } else {
                System.out.println("Metodo nao suportado ou argumentos incorretos.");
                return;
            }

            method.invoke(player, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}