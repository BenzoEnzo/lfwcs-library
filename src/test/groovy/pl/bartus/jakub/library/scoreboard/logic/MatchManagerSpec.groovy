package pl.bartus.jakub.library.scoreboard.logic

import pl.bartus.jakub.library.scoreboard.exception.ScoreBoardException
import pl.bartus.jakub.library.scoreboard.model.Score
import pl.bartus.jakub.library.scoreboard.model.Team
import spock.lang.Shared
import spock.lang.Specification

class MatchManagerSpec extends Specification {

    def matchManager
    @Shared
    def teamA, teamB, teamC

    def setup() {
        matchManager = new MatchManager()
        teamA = new Team("Polska")
        teamB = new Team("fRANCJA")
        teamC = new Team("Niemcy")
    }

    def "Should return an empty collection of ongoing matches"() {
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

    def "Should throw a ScoreBoardException because team names are equal"() {

        when:
        matchManager.addNewMatch(teamA, teamA)

        then:
        thrown(ScoreBoardException)
    }

    def "Should throw a ScoreBoardException because the team already exists in the collection"() {

        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.addNewMatch(teamA, teamC)

        then:
        thrown(ScoreBoardException)
    }

    def "Should throw a ScoreBoardException during update a score because one of the inputs has negative value"() {
        given:
        def score = new Score(-5,4);

        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.updateMatchScore(teamA, score)

        then:
        thrown(ScoreBoardException)
    }

    def "Should throw a ScoreBoardException  when updating the score of a non existent match"() {
        given:
        def score = new Score(4,5)

        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.updateMatchScore(teamC, score)

        then:
        thrown(ScoreBoardException)
    }

    def "Should correctly update the score of an existing match"() {
        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.updateMatchScore(updateTeam, new Score(homePoints,awayPoints))
        def match = matchManager.findAllOngoingMatches().find {
            it.homeTeam == updateTeam || it.awayTeam == updateTeam
        }

        then:
        match.score.homePoints == homePoints
        match.score.awayPoints == awayPoints
        match.totalScore == totalScore

        where:
        updateTeam | homePoints | awayPoints | totalScore
        teamA      | 20         | 10         | 30
    }

    def "Should return ongoing matches in correct order by totalScore then by date"() {
        given:
        def teamD = new Team("Islandia")
        def teamE = new Team("Szkocja")
        def teamF = new Team("Czechy")
        def scoreA = new Score(20,10)
        def scoreC = new Score(90,5)
        def scoreE = new Score(5,90)

        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.addNewMatch(teamC, teamD)
        Thread.sleep(100)
        matchManager.addNewMatch(teamE, teamF)
        matchManager.updateMatchScore(teamC, scoreC)
        matchManager.updateMatchScore(teamE, scoreE)
        matchManager.updateMatchScore(teamA, scoreA)

        def matches = matchManager.findAllOngoingMatches()

        then:
        matches.size() == 3
        matches.first().homeTeam.name == "Szkocja"
        matches.first().awayTeam.name == "Czechy"
        matches.last().homeTeam.name == "Polska"
        matches.last().awayTeam.name == "Francja"
    }

    def "Should correctly delete a match from the scoreboard"() {
        when:
        matchManager.addNewMatch(teamA, teamB)
        matchManager.finishMatch(teamA)
        def matches = matchManager.findAllOngoingMatches()

        then:
        matches.size() == 0
    }
}