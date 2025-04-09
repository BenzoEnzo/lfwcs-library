package pl.bartus.jakub.library.scoreboard.logic

import pl.bartus.jakub.library.scoreboard.exception.ScoreBoardException
import pl.bartus.jakub.library.scoreboard.model.Team
import spock.lang.Shared
import spock.lang.Specification

class MatchManagerSpec extends Specification {

    def matchManager
    @Shared def teamA, teamB, teamC

    def setup() {
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
        matchManager.addNewMatch(teamA, teamB)
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
        matchManager.addNewMatch(teamA, teamA)

        then:
        thrown(ScoreBoardException)
    }

    def "Should throw a TeamHasActiveMatchException because the team already exists in the collection"() {

        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.addNewMatch(teamA, teamC)

        then:
        thrown(TeamHasActiveMatchException)
    }

    def "Should throw an IllegalArgumentException during update a score because one of the inputs has negative value"() {
        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.updateMatchScore(teamA, -5, 4)

        then:
        thrown(ScoreBoardException)
    }

    def "Should throw an InvalidTeamException when updating the score of a non existent match"() {
        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.updateMatchScore(teamC, 4, 5)

        then:
        thrown(ScoreBoardException)
    }

    def "Should correctly update the score of an existing match"() {

        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.updateMatchScore(updateTeam,homePoints,awayPoints)
        def match = matchManager.findAllOngoingMatches().find {
            it.homeTeam == updateTeam || it.awayTeam == updateTeam
        }

        then:
        match.score.homePoints == homePoints
        match.score.awayPoints == awayPoints
        match.totalScore == totalScore

        where:
        updateTeam | homePoints | awayPoints | totalScore
        teamA      | 20         | 10         |  30
        teamB      | 40         | 5          |  45
    }

    def "Should return ongoing matches in correct order by totalScore and by date"() {
        given:
        def teamD = new Team("Islandia")
        def teamE = new Team("Szkocja")
        def teamF = new Team("Czechy")

        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.addNewMatch(teamC, teamD)
        matchManager.addNewMatch(teamE, teamF)
        matchManager.updateMatchScore(teamC, 90, 5)
        matchManager.updateMatchScore(teamE, 5, 90)
        matchManager.updateMatchScore(teamA, 20, 10)

        def matches = matchManager.findAllOngoingMatches().toList()

        then:
        matches.size() == 3
        matches.first().homeTeam.name == "Niemcy"
        matches.first().awayTeam.name == "Islandia"
        matches.last().homeTeam.name == "Polska"
        matches.last().awayTeam.name == "Francja"
    }

    def ""() {

    }
}