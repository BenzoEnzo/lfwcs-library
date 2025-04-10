package pl.bartus.jakub.library.scoreboard.model;

import pl.bartus.jakub.library.scoreboard.exception.ScoreBoardException;

public record Team(String name) {
    public Team {
        if (name.isEmpty()) {
            throw new ScoreBoardException("Team name cannot be empty");
        }
        name = name.toLowerCase();
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}

