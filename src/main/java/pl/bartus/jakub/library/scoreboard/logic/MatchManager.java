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

    public void finishMatch(@NonNull Team team) {
        matches.remove(team);
    }

    public List<Match> findAllOngoingMatches() {
        return matches.values()
                .stream()
                .sorted(new MatchComparator())
                .toList();
    }

    public void updateMatchScore(@NonNull Team team, int homePoints, int awayPoints) {
        if (homePoints < 0 || awayPoints < 0) {
            throw new ScoreBoardException("Score values must be non-negative");
        }

        Match match = matches.get(team);

        if (match == null) {
            throw new ScoreBoardException("Team not found in Score Board");
        }

        match.setScore(new Score(homePoints, awayPoints));
        match.calculateTotalScore();
    }

    public void addNewMatch(Team homeTeam, Team awayTeam) {
        validateTeams(homeTeam, awayTeam);

        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();

        matches.put(homeTeam, match);
    }

    private boolean isInOngoingMatch(Team team) {
        return matches.values().stream().anyMatch(t -> t.isIncludeTeam(team));
    }

    private void validateTeams(Team homeTeam, Team awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new ScoreBoardException("Team cannot be null");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new ScoreBoardException("Team must be different");
        }

        if (isInOngoingMatch(homeTeam) || isInOngoingMatch(awayTeam)) {
            throw new ScoreBoardException("Team is in an ongoing match");
        }
    }
}
