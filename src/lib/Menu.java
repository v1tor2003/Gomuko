package src.lib;

import java.io.Serializable;
import java.util.Scanner;

import utils.ConsoleCleaner;
import utils.UnixConsoleCleaner;
import utils.WindowsConsoleCleaner;

public class Menu implements Serializable{
    private static final Scanner scanner = new Scanner(System.in);
    private ConsoleCleaner cc;
    private String[] options;
    private static final char exitOption = 'q';
    
    public Menu () {
        this.cc = Menu.getConsoleCleaner(System.getProperty("os.name"));
    }

    public void greet(String[] messages) {
        this.clearScreen();
        System.out.println(String.join("\n", messages));
    }

    public void displayOptions() {
        System.out.println(String.join("\n", this.options));
    }

    public char getUserInput(){
        return scanner
                .nextLine()
                .toLowerCase()
                .trim()
                .charAt(0);
    }

    public char getChoice(){
        this.displayOptions();
        char in = this.getUserInput();
        this.clearScreen();

        return in;
    }

    public int getIntInput() {
        while (true) {
            try {
                int num = this.getScanner().nextInt();
                //this.getScanner().nextLine();
                return num;
            } catch (Exception e) {
                System.out.println("Entrada invalida. Por favor, insira um numero.");
                this.getScanner().nextLine(); 
            }
        }
    }

    public char getExitOption () {
        return exitOption;
    }

    public void setOptions(String[] options){
        this.options = options;
    }

    public void exit(int code){
        System.out.println("Saindo...");
        System.exit(code);
    }

    public void exit(){
        this.exit(0);
    }

    public String getPlayerName() {
        return scanner
                .nextLine()
                .trim();
    }

    public Scanner getScanner(){
        return scanner;
    }

    public void clearScreen() {
        this.cc.clearScreen();
    }

    public void close(){
        scanner.close();
    }

    private static ConsoleCleaner getConsoleCleaner(String os) {
        return os.contains("Windows") ? new WindowsConsoleCleaner() : new UnixConsoleCleaner();
    }
}
