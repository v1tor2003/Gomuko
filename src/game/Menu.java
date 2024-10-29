package src.game;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private String[] options;
    private final static Scanner scanner = new Scanner(System.in);
    
    public Menu(String[] options) {
        this.options = options;
    }

    public void display() {
        for (int i = 0; i < options.length; i++) {
            System.out.println("%d. %s".formatted((i + 1), options[1]));
        }
    }

    private boolean isValidChoice(int choice) {
        return choice < 1 || choice > options.length;
    }

    public int getChoice(){
        int choice = -1;
        
        while (!isValidChoice(choice)) {
            System.out.println("Enter your choice (1-" + options.length + "): ");
            if(scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if(!isValidChoice(choice))
                    System.out.println("Invalid choice, try again.");
            } else {
                System.out.println("Invalid input, enter a number.");
                scanner.next();
            }
        }

        return choice;
    }

    public void close () {
        scanner.close();
    }

    public void clearScreen () {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
              new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
              System.out.print("\033[H\033[2J");
              System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error clearing console: " + ex.getMessage());
        }
    }
    
}
