package pl.bartus.jakub.library.scoreboard.logic;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.bartus.jakub.library.scoreboard.exception.InvalidTeamException;
import pl.bartus.jakub.library.scoreboard.exception.TeamHasActiveMatchException;
import pl.bartus.jakub.library.scoreboard.model.Match;
import pl.bartus.jakub.library.scoreboard.model.Team;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class MatchManager {
    private final Set<Match> matches = new HashSet<>();

    public Set<Match> findAllOngoingMatches() {
        return matches;
    }

    public void addNewMatch(Team homeTeam, Team awayTeam) {
        if (isTeamInOngoingMatch(homeTeam) || isTeamInOngoingMatch(awayTeam)) {
            throw new TeamHasActiveMatchException("One of the teams is already in a match");
        }

        Match match = initializeMatch(homeTeam, awayTeam);

        matches.add(match);
    }

    private boolean isTeamInOngoingMatch(@NonNull Team team) {
        return matches.stream()
                .anyMatch(match -> match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team));
    }

    private Match initializeMatch(Team homeTeam, Team awayTeam) {
        validateTeams(homeTeam, awayTeam);

        return Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();
    }

    private void validateTeams(Team homeTeam, Team awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new InvalidTeamException("Team names cannot be null");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new InvalidTeamException("Team names must be different");
        }
    }
}
