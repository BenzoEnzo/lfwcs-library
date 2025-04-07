package pl.bartus.jakub.library.scoreboard.logic

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
}