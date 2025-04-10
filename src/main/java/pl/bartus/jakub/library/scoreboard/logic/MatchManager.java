package pl.bartus.jakub.library.scoreboard.logic;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.bartus.jakub.library.scoreboard.exception.ScoreBoardException;
import pl.bartus.jakub.library.scoreboard.model.Match;
import pl.bartus.jakub.library.scoreboard.model.Score;
import pl.bartus.jakub.library.scoreboard.model.Team;

import java.util.*;

@NoArgsConstructor
public class MatchManager {
    private final Map<Team, Match> matches = new HashMap<>();

    public void finishMatch(@NonNull Team homeTeam) {
        matches.remove(homeTeam);
    }

    public List<Match> findAllOngoingMatches() {
        return matches.values()
                .stream()
                .sorted(new MatchComparator())
                .toList();
    }

    public void updateMatchScore(@NonNull Team homeTeam, @NonNull Score score) {
        if (score.homePoints() < 0 || score.awayPoints() < 0) {
            throw new ScoreBoardException("Score values must be non-negative");
        }

        Match match = matches.get(homeTeam);

        if (match == null) {
            throw new ScoreBoardException("Team not found in Score Board");
        }

        match.setScore(score);
        match.calculateTotalScore();
    }

    public void addNewMatch(@NonNull Team homeTeam, @NonNull Team awayTeam) {
        validateTeams(homeTeam, awayTeam);

        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();

        matches.put(homeTeam, match);
    }

    private boolean isInOngoingMatch(Team team) {
        return matches.values().stream().anyMatch(t -> t.containsTeam(team));
    }

    private void validateTeams(Team homeTeam, Team awayTeam) {
        if (homeTeam.equals(awayTeam)) {
            throw new ScoreBoardException("Home team and Away team cannot be the same");
        }

        if (isInOngoingMatch(homeTeam) || isInOngoingMatch(awayTeam)) {
            throw new ScoreBoardException("Team is in the ongoing match");
        }
    }
}
