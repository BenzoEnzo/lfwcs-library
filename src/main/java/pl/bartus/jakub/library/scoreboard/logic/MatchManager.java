package pl.bartus.jakub.library.scoreboard.logic;

import lombok.NoArgsConstructor;
import pl.bartus.jakub.library.scoreboard.model.Match;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class MatchManager {
    public final Set<Match> matches = new HashSet<>();

    public Set<Match> findAllOngoingMatches(){
        return matches;
    }
}
