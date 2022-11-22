package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ShipCheckerAndMakerTest {
    @Test
    fun `ShipChecker does nothing if ship seen`() {
        val ship = Ship(U.randomPoint())
        val checker = ShipChecker(ship)
        checker.interactions.beforeInteractions()
        val transaction = Transaction()
        checker.interactions.newInteract(ship, true, transaction)
        val nothing = transaction.removes.toList()
        assertThat(nothing).isEmpty()
        val emptyTransaction = checker.interactions.afterInteractions()
        assertThat(emptyTransaction.adds).isEmpty()
        assertThat(emptyTransaction.removes).isEmpty()
        val alsoNothing = checker.tick(0.01)
        assertThat(alsoNothing).isEmpty()
    }

    @Test
    fun `ShipChecker does nothing if ship seen via withOther`() {
        val ship = Ship(U.randomPoint())
        val checker = ShipChecker(ship)
        checker.interactions.beforeInteractions()
        val transaction = Transaction()
        checker.interactions.newInteract(ship, true, transaction)
        val nothing = transaction.removes.toList()
        assertThat(nothing).isEmpty()
        val emptyTransaction = checker.interactions.afterInteractions()
        assertThat(emptyTransaction.adds).isEmpty()
        assertThat(emptyTransaction.removes).isEmpty()
        val alsoNothing = checker.tick(0.01)
        assertThat(alsoNothing).isEmpty()
    }

    @Test
    fun `creates ShipMaker if no ship seen`() {
        val ship = Ship(U.randomPoint())
        val checker = ShipChecker(ship)
        checker.interactions.beforeInteractions()
        // we see no ship here
        val transaction = checker.interactions.afterInteractions()
        assertThat(transaction.removes.toList()[0]).isEqualTo(checker)
        assertThat(transaction.adds.toList()[0]).isInstanceOf(ShipMaker::class.java)
    }

    @Test
    fun `maker delays U MAKER_DELAY seconds`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val maker = ShipMaker(ship)
        maker.tick(U.MAKER_DELAY)
        maker.interactions.beforeInteractions()
        // nothing in the way
        val nothing = maker.interactions.afterInteractions()
        assertThat(nothing.adds).isEmpty()
        assertThat(nothing.removes).isEmpty()
    }

    @Test
    fun `maker makes after U MAKER_DELAY seconds`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        ship.velocity = Velocity(50.0, 60.0)
        ship.heading = 90.0
        val maker = ShipMaker(ship)
        maker.tick(U.MAKER_DELAY)
        maker.tick(0.01)
        maker.interactions.beforeInteractions()
        // nothing in the way
        val trans = maker.interactions.afterInteractions()
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(trans.firstRemove()).isEqualTo(maker)
    }

    @Test
    fun `maker makes only when safe`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val asteroid = Asteroid(
            U.CENTER_OF_UNIVERSE
        )
        val maker = ShipMaker(ship)
        maker.tick(U.MAKER_DELAY)
        maker.tick(0.01)
        maker.interactions.beforeInteractions()
        val transaction = Transaction()
        maker.interactions.newInteract(asteroid, true, transaction)
        transaction.removes.toList()
        val nothing = maker.interactions.afterInteractions()
        assertThat(nothing.adds).isEmpty()
        assertThat(nothing.removes).isEmpty()
        maker.interactions.beforeInteractions()
        // nothing
        val trans = maker.interactions.afterInteractions()
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(trans.firstRemove()).isEqualTo(maker)
        assertThat(ship.velocity).isEqualTo(Velocity.ZERO)
        assertThat(ship.heading).isEqualTo(0.0)
    }

    @Test
    fun `makes with ship features unchanged`() {
        val position = Point(123.0, 456.0)
        val velocity = Velocity(200.0, 300.0)
        val heading = 37.5
        val ship = Ship(position)
        ship.heading = heading
        ship.velocity = velocity
        val maker = ShipMaker(ship)
        maker.tick(U.MAKER_DELAY + 0.01)
        maker.interactions.beforeInteractions()
        // no obstacles
        maker.asteroidTally = 60 // no possible hyperspace failure
        val trans = maker.interactions.afterInteractions()
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(ship.position).isEqualTo(position)
        assertThat(ship.velocity).isEqualTo(velocity)
        assertThat(ship.heading).isEqualTo(heading)
    }

    @Test
    fun `maker counts asteroids`() {
        val a = Asteroid(
            U.randomPoint()
        )
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val maker = ShipMaker(ship)
        maker.interactions.beforeInteractions()
        val transaction = Transaction()
        maker.interactions.newInteract(a, true, transaction)
        transaction.removes.toList()
        val transaction1 = Transaction()
        maker.interactions.newInteract(a, true, transaction1)
        transaction1.removes.toList()
        assertThat(maker.asteroidTally).isEqualTo(2)
    }

    @Test
    fun `hyperspace failure checks`() {
        val ship = Ship(Point(10.0, 10.0))
        val hyper = HyperspaceOperation(ship, 0)
        assertThat(hyper.hyperspaceFailure(62, 19)).describedAs("roll 62 19 asteroids").isEqualTo(false)
        assertThat(hyper.hyperspaceFailure(62, 18)).describedAs("roll 62 18 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(45, 0)).describedAs("roll 45 0 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(44, 0)).describedAs("roll 44 0 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(43, 0)).describedAs("roll 43 0 asteroids").isEqualTo(false)
    }
}