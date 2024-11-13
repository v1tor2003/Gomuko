package src.lib;

import src.interfaces.GameLogger;

public class LoggerIpml implements GameLogger {
    @Override
    public void logInfo(String[] info){
        StringBuilder sb = new StringBuilder();
        for (String string : info) sb.append(string + "\n");
        String time = java.time.LocalDateTime.now().toLocalDate().toString();
        System.out.println("[%s][LOG]: %s".formatted(time, sb.toString()));
    }
    
    @Override
    public void logInfo(String info){
        this.logInfo(new String[] {info});
    }
}
