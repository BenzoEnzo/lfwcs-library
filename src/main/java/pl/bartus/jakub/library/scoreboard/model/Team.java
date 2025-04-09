package pl.bartus.jakub.library.scoreboard.model;

public record Team(String name) {
    public Team {
        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
    }
}
