package console;
import java.io.Serializable;

@FunctionalInterface
public interface ConsoleCleaner extends Serializable {
    void clearScreen();
}
