package pl.bartus.jakub.library.scoreboard.exception;

public class TeamHasActiveMatchException extends RuntimeException {
    public TeamHasActiveMatchException(String message){
        super(message);
    }
}
