package utils;

public class UnixConsoleCleaner implements ConsoleCleaner{
    @Override
    public void clearScreen() {
        try {
            Runtime.getRuntime().exec(new String []{"clear"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
