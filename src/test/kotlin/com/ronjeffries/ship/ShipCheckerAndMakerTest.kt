package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class ShipCheckerAndMakerTest {
    @Test
    fun `ShipChecker does nothing if ship seen`() {
        val ship = Ship(
            position = U.randomPoint()
        )
        val checker = ShipChecker(ship)
        checker.subscriptions.beforeInteractions()
        val nothing = Transaction()
        checker.subscriptions.interactWithShip(ship, nothing)
        assertThat(nothing.removes).isEmpty()
        val emptyTransaction = Transaction()
        checker.subscriptions.afterInteractions(emptyTransaction)
        assertThat(emptyTransaction.adds).isEmpty()
        assertThat(emptyTransaction.removes).isEmpty()
        val alsoNothing = Transaction()
        checker.update(0.01, alsoNothing)
        assertThat(alsoNothing.adds).isEmpty()
        assertThat(alsoNothing.removes).isEmpty()
    }

    @Test
    fun `ship randomizes position on hyperspace entry`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val trans = Transaction()
        ship.enterHyperspace(trans)
        assertThat(trans.firstRemove()).isEqualTo(ship)
        ship.finalize()
        assertThat(ship.position).isNotEqualTo(U.CENTER_OF_UNIVERSE)
    }

    @Test
    fun `ship goes to center on collision death`() {
        val ship = Ship(U.randomPoint())
        val trans = Transaction()
        ship.collision(trans)
        assertThat(trans.firstRemove()).isEqualTo(ship)
        ship.finalize()
        assertThat(ship.position).isEqualTo(U.CENTER_OF_UNIVERSE)
    }

    @Test
    fun `creates ShipMaker if no ship seen`() {
        val ship = Ship(
            position = U.randomPoint()
        )
        val checker = ShipChecker(ship)
        checker.subscriptions.beforeInteractions()
        // we see no ship here
        val transaction = Transaction()
        checker.subscriptions.afterInteractions(transaction)
        assertThat(transaction.removes.toList()[0]).isEqualTo(checker)
        assertThat(transaction.adds.toList()[0]).isInstanceOf(ShipMaker::class.java)
    }

    @Test
    fun `debits ScoreKeeper if ship is dead`() {
        val scoreKeeper = ScoreKeeper(1)
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val checker = ShipChecker(ship, scoreKeeper)
        checker.subscriptions.beforeInteractions()
        val trans = Transaction()
        checker.subscriptions.afterInteractions(trans)
        assertThat(scoreKeeper.shipCount).isEqualTo(0)
    }

    @Test
    fun `does not debit ScoreKeeper if ship is in hyperspace`() {
        val scoreKeeper = ScoreKeeper(1)
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val removalTrans = Transaction()
        ship.enterHyperspace(removalTrans)
        assertThat(removalTrans.firstRemove()).isEqualTo(ship)
        val checker = ShipChecker(ship, scoreKeeper)
        checker.subscriptions.beforeInteractions()
        val makerTrans = Transaction()
        checker.subscriptions.afterInteractions(makerTrans)
        assertThat(scoreKeeper.shipCount).isEqualTo(1)
    }

    @Test
    fun `maker delays U MAKER_DELAY seconds`() {
        val ship = Ship(
            position = U.CENTER_OF_UNIVERSE
        )
        val maker = ShipMaker(ship)
        maker.update(U.MAKER_DELAY, Transaction())
        maker.subscriptions.beforeInteractions()
        // nothing in the way
        val nothing = Transaction()
        maker.subscriptions.afterInteractions(nothing)
        assertThat(nothing.adds).isEmpty()
        assertThat(nothing.removes).isEmpty()
    }

    @Test
    fun `maker makes after U MAKER_DELAY seconds`() {
        val ship = Ship(
            position = U.CENTER_OF_UNIVERSE
        )
        ship.velocity = Velocity(50.0, 60.0)
        ship.heading = 90.0
        val maker = ShipMaker(ship)
        val ignored = Transaction()
        maker.update(U.MAKER_DELAY, ignored)
        maker.update(0.01, ignored)
        maker.subscriptions.beforeInteractions()
        // nothing in the way
        val trans = Transaction()
        maker.subscriptions.afterInteractions(trans)
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(trans.firstRemove()).isEqualTo(maker)
    }

    @Test
    fun `asteroid clears safeToEmerge`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        val maker = ShipMaker(ship)
        val ignored = Transaction()
        maker.subscriptions.beforeInteractions()
        assertThat(maker.safeToEmerge).isEqualTo(true)
        maker.subscriptions.interactWithAsteroid(asteroid, ignored)
        assertThat(maker.safeToEmerge).isEqualTo(false)
    }

    @Test
    fun `saucer clears safeToEmerge`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val saucer = Saucer()
        saucer.position = U.CENTER_OF_UNIVERSE
        val maker = ShipMaker(ship)
        val ignored = Transaction()
        maker.subscriptions.beforeInteractions()
        assertThat(maker.safeToEmerge).isEqualTo(true)
        maker.subscriptions.interactWithSaucer(saucer, ignored)
        assertThat(maker.safeToEmerge).isEqualTo(false)
    }

    @Test
    fun `missile clears safeToEmerge`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val missile = Missile(Point.ZERO)
        val maker = ShipMaker(ship)
        val ignored = Transaction()
        maker.subscriptions.beforeInteractions()
        assertThat(maker.safeToEmerge).isEqualTo(true)
        maker.subscriptions.interactWithMissile(missile, ignored)
        assertThat(maker.safeToEmerge).isEqualTo(false)
    }

    @Test
    fun `maker makes only when safe`() {
        val ship = Ship(
            position = U.CENTER_OF_UNIVERSE
        )
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        val maker = ShipMaker(ship)
        val ignored = Transaction()
        maker.update(U.MAKER_DELAY, ignored)
        maker.update(0.01, ignored)
        maker.subscriptions.beforeInteractions()
        val notInteresting = Transaction()
        maker.subscriptions.interactWithAsteroid(asteroid, notInteresting)
        val nothing = Transaction()
        maker.subscriptions.afterInteractions(nothing)
        assertThat(nothing.adds).isEmpty()
        assertThat(nothing.removes).isEmpty()
        maker.subscriptions.beforeInteractions()
        // nothing
        val trans = Transaction()
        maker.subscriptions.afterInteractions(trans)
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
        val ship = Ship(
            position = position
        )
        ship.heading = heading
        ship.velocity = velocity
        val maker = ShipMaker(ship)
        maker.update(U.MAKER_DELAY + 0.01, Transaction())
        maker.subscriptions.beforeInteractions()
        // no obstacles
        maker.asteroidTally = 60 // no possible hyperspace failure
        val trans = Transaction()
        maker.subscriptions.afterInteractions(trans)
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(ship.position).isEqualTo(position)
        assertThat(ship.velocity).isEqualTo(velocity)
        assertThat(ship.heading).isEqualTo(heading)
    }

    @Test
    fun `maker counts asteroids`() {
        val a = Asteroid(U.randomPoint())
        val ship = Ship(
            position = U.CENTER_OF_UNIVERSE
        )
        val maker = ShipMaker(ship)
        maker.subscriptions.beforeInteractions()
        val trans = Transaction()
        maker.subscriptions.interactWithAsteroid(a, trans)
        maker.subscriptions.interactWithAsteroid(a, trans)
        assertThat(maker.asteroidTally).isEqualTo(2)
    }

    @Test
    fun `hyperspace failure checks`() {
        val ignoredShip = Ship(U.CENTER_OF_UNIVERSE)
        val hyper = ShipMaker(ignoredShip)
        assertThat(hyper.hyperspaceFailure(62, 19)).describedAs("roll 62 19 asteroids").isEqualTo(false)
        assertThat(hyper.hyperspaceFailure(62, 18)).describedAs("roll 62 18 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(45, 0)).describedAs("roll 45 0 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(44, 0)).describedAs("roll 44 0 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(43, 0)).describedAs("roll 43 0 asteroids").isEqualTo(false)
    }
}