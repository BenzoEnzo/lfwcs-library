package pl.bartus.jakub.library.scoreboard.logic;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.bartus.jakub.library.scoreboard.exception.ScoreBoardException;
import pl.bartus.jakub.library.scoreboard.model.Match;
import pl.bartus.jakub.library.scoreboard.model.Score;
import pl.bartus.jakub.library.scoreboard.model.Team;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MatchManager {
    private final Set<Match> matches = new HashSet<>();

    public void finishMatch(@NonNull Team team) {
        Match match = findMatchByTeam(team);
        matches.remove(match);
    }

    public Set<Match> findAllOngoingMatches() {
        return matches.stream()
                .sorted(new MatchComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void updateMatchScore(Team team, int homePoints, int awayPoints) {
        if (homePoints < 0 || awayPoints < 0) {
            throw new ScoreBoardException("Score values must be non-negative");
        }

        Match match = findMatchByTeam(team);

        match.setScore(new Score(homePoints, awayPoints));
        match.calculateTotalScore();
    }

    public void addNewMatch(Team homeTeam, Team awayTeam) {
        validateTeams(homeTeam, awayTeam);

        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();

        matches.add(match);
    }

    private Match findMatchByTeam(Team team) {
        return matches.stream()
                .filter(match -> match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team))
                .findFirst()
                .orElseThrow(() -> new ScoreBoardException("No match found with the given team"));
    }

    private boolean isTeamInOngoingMatch(@NonNull Team team) {
        return matches.stream()
                .anyMatch(match -> match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team));
    }

    private void validateTeams(Team homeTeam, Team awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new ScoreBoardException("Team cannot be null");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new ScoreBoardException("Team must be different");
        }

        if (isTeamInOngoingMatch(homeTeam) || isTeamInOngoingMatch(awayTeam)) {
            throw new ScoreBoardException("One of the teams is already in a match");
        }
    }
}
