package pl.bartus.jakub.library.scoreboard.logic

import pl.bartus.jakub.library.scoreboard.exception.InvalidTeamException
import spock.lang.Specification

class MatchManagerSpec extends Specification {

    def "Should return an empty set of ongoing matches"() {
        given:
        def matchManager = new MatchManager()

        when:
        def matches = matchManager.findAllOngoingMatches()

        then:
        matches.isEmpty()
    }

    def "Should add a new match to collection with ongoing matches"() {
        given:
        def matchManager = new MatchManager()

        when:
        matchManager.addNewMatch("Polska","Francja")
        def matches = matchManager.findAllOngoingMatches()

        then:
        matches.size() == 1
        matches.first().homeTeam.name() == "Polska"
        matches.first().awayTeam.name() == "Francja"
        matches.first().getScore().awayPoints() == 0
        matches.first().getScore().homePoints() == 0
        matches.first().totalScore == 0
    }

    def "Should throw an InvalidTeamException because team names are equal"() {
        given:
        def matchManager = new MatchManager()

        when:
        matchManager.addNewMatch("Polska","Polska")

        then:
        thrown(InvalidTeamException)
    }
}