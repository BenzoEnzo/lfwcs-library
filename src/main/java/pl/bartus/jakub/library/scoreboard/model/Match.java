package pl.bartus.jakub.library.scoreboard.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private Score score;
    private int totalScore;
    private final LocalDateTime startedAt = LocalDateTime.now();
}
