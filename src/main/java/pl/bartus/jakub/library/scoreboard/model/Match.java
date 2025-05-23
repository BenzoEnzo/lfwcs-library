package pl.bartus.jakub.library.scoreboard.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    @Builder.Default
    private Score score = new Score(0, 0);
    @Builder.Default
    private int totalScore = 0;
    private final LocalDateTime startedAt = LocalDateTime.now();

    public void calculateTotalScore() {
        this.totalScore = score.homePoints() + score.awayPoints();
    }

    public boolean containsTeam(Team team) {
        return homeTeam.equals(team) || awayTeam.equals(team);
    }

    @Override
    public String toString() {
        return homeTeam.name() + " " +
                score.homePoints() + " - " +
                awayTeam.name() + " " +
                score.awayPoints();
    }
}
