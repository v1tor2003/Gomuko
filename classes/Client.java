package classes;

import interfaces.RMIServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    private static final Scanner scanner = new Scanner(System.in);
    public Client () {}

    public static void main (String ...args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            RMIServer serverStub = (RMIServer) registry.lookup("RMIServer");
            
            System.out.println("Enter you username: ");
            var player = new Player(scanner.nextLine());

            System.out.println("Join a game (J) | Create a game (C): ");
            char input = scanner
                        .nextLine()
                        .toLowerCase()
                        .charAt(0);
            
            switch (input) {
                case 'c' -> {
                    System.out.println("Creating a new game lobby for player: " + player.toString());
                    int lobbyId = serverStub.createGame(player);
                    System.out.println("Created lobby with id: " + lobbyId);
                    
                }
                case 'j' -> {
                    System.out.println("Enter lobby id:");
                    int lobbyId = scanner.nextInt();
                    System.out.println("Joining lobby with id: " + lobbyId);
                    serverStub.join(lobbyId, player);
                }
                default -> { }
            }

            System.out.println(serverStub.getGameState(1));
        } catch (Exception e) {
            System.err.println("Client Exception: " + e.toString());
            e.printStackTrace();
        }
        
    }
}
