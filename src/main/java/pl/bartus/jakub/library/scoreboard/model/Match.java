package pl.bartus.jakub.library.scoreboard.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class Match {
    private Team homeTeam;
    private Team awayTeam;
    @Builder.Default
    private Score score = new Score(0,0);
    @Builder.Default
    private int totalScore = 0;
    private final LocalDateTime startedAt = LocalDateTime.now();

    public void calculateTotalScore(){
        this.totalScore = score.homePoints() + score.awayPoints();
    }
}
