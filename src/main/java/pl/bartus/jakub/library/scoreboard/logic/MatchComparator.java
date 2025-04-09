package pl.bartus.jakub.library.scoreboard.logic;

import pl.bartus.jakub.library.scoreboard.model.Match;

import java.util.Comparator;

public class MatchComparator implements Comparator<Match> {
    @Override
    public int compare(Match o1, Match o2) {
        int scoreComparison = Integer.compare(o2.getTotalScore(), o1.getTotalScore());

        if (scoreComparison == 0) {
            return o2.getStartedAt().compareTo(o1.getStartedAt());
        }

        return scoreComparison;
    }
}
