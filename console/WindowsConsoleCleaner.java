package console;

public class WindowsConsoleCleaner implements ConsoleCleaner {
    @Override
    public void clearScreen() {
        try {
            new ProcessBuilder("cmd" ,"/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
