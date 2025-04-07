package pl.bartus.jakub.library.scoreboard.logic;

import lombok.NoArgsConstructor;
import pl.bartus.jakub.library.scoreboard.exception.InvalidTeamException;
import pl.bartus.jakub.library.scoreboard.model.Match;
import pl.bartus.jakub.library.scoreboard.model.Team;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class MatchManager {
    public final Set<Match> matches = new HashSet<>();

    public Set<Match> findAllOngoingMatches() {
        return matches;
    }

    public void addNewMatch(String homeTeamName, String awayTeamName) {
        Match match = initializeMatch(homeTeamName, awayTeamName);
        matches.add(match);
    }

    private Match initializeMatch(String homeTeamName, String awayTeamName) {
        validateTeams(homeTeamName, awayTeamName);

        return Match.builder()
                .homeTeam(new Team(homeTeamName))
                .awayTeam(new Team(awayTeamName))
                .build();
    }

    private void validateTeams(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new InvalidTeamException("Team names cannot be null");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new InvalidTeamException("Team names must be different");
        }
    }
}
