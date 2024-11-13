package console;

public abstract class ConsoleCleanerFactory {
    public static ConsoleCleaner getConsoleCleaner(String os){
        return os.contains("Windows") 
                ? new WindowsConsoleCleaner() 
                : new UnixConsoleCleaner();
    }    
}