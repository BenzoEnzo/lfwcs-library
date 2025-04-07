package pl.bartus.jakub.library.scoreboard.logic

import pl.bartus.jakub.library.scoreboard.exception.InvalidTeamException
import pl.bartus.jakub.library.scoreboard.exception.TeamHasActiveMatchException
import pl.bartus.jakub.library.scoreboard.model.Team
import spock.lang.Specification

class MatchManagerSpec extends Specification {

    def matchManager
    def teamA, teamB, teamC

    def setup(){
        matchManager = new MatchManager()
        teamA = new Team("Polska")
        teamB = new Team("Francja")
        teamC = new Team("Niemcy")
    }

    def "Should return an empty set of ongoing matches"() {
        when:
        def matches = matchManager.findAllOngoingMatches()

        then:
        matches.isEmpty()
    }

    def "Should add a new match to collection with ongoing matches"() {

        when:
        matchManager.addNewMatch(teamA,teamB)
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

        when:
        matchManager.addNewMatch(teamA,teamA)

        then:
        thrown(InvalidTeamException)
    }

    def "Should throw a TeamHasActiveMatchException because the team already exists in the collection"() {

        when:
        matchManager.addNewMatch(teamA,teamB)
        matchManager.addNewMatch(teamA,teamC)

        then:
        thrown(TeamHasActiveMatchException)
    }

}