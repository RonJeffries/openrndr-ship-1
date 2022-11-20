package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class ShipCheckerAndMakerTest {
    @Test
    fun `ShipChecker does nothing if ship seen`() {
        val ship = SolidObject.ship(U.randomPoint())
        val checker = ShipChecker(ship)
        checker.beginInteraction()
        val nothing = checker.interactWith(ship)
        assertThat(nothing).isEmpty()
        val emptyTransaction = checker.finishInteraction()
        assertThat(emptyTransaction.adds).isEmpty()
        assertThat(emptyTransaction.removes).isEmpty()
        val alsoNothing = Transaction()
        checker.tick(0.01, alsoNothing)
        assertThat(alsoNothing.adds).isEmpty()
        assertThat(alsoNothing.removes).isEmpty()
    }

    @Test
    fun `ShipChecker does nothing if ship seen via withOther`() {
        val ship = SolidObject.ship(U.randomPoint())
        val checker = ShipChecker(ship)
        checker.beginInteraction()
        val nothing = checker.interactWithOther(ship)
        assertThat(nothing).isEmpty()
        val emptyTransaction = checker.finishInteraction()
        assertThat(emptyTransaction.adds).isEmpty()
        assertThat(emptyTransaction.removes).isEmpty()
        val alsoNothing = Transaction()
        checker.tick(0.01, alsoNothing)
        assertThat(alsoNothing.adds).isEmpty()
        assertThat(alsoNothing.removes).isEmpty()
    }

    @Test
    fun `creates ShipMaker if no ship seen`() {
        val ship = SolidObject.ship(U.randomPoint())
        val checker = ShipChecker(ship)
        checker.beginInteraction()
        // we see no ship here
        val transaction = checker.finishInteraction()
        assertThat(transaction.removes.toList()[0]).isEqualTo(checker)
        assertThat(transaction.adds.toList()[0]).isInstanceOf(ShipMaker::class.java)
    }

    @Test
    fun `maker delays U MAKER_DELAY seconds`() {
        val ship = SolidObject.ship(U.CENTER_OF_UNIVERSE)
        val maker = ShipMaker(ship)
        maker.tick(U.MAKER_DELAY, Transaction())
        maker.beginInteraction()
        // nothing in the way
        val nothing = maker.finishInteraction()
        assertThat(nothing.adds).isEmpty()
        assertThat(nothing.removes).isEmpty()
    }

    @Test
    fun `maker makes after U MAKER_DELAY seconds`() {
        val ship = SolidObject.ship(U.CENTER_OF_UNIVERSE)
        ship.velocity = Velocity(50.0,60.0)
        ship.heading = 90.0
        val maker = ShipMaker(ship)
        val ignored = Transaction()
        maker.tick(U.MAKER_DELAY, ignored)
        maker.tick(0.01, ignored)
        maker.beginInteraction()
        // nothing in the way
        val trans = maker.finishInteraction()
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(trans.firstRemove()).isEqualTo(maker)
    }

    @Test
    fun `maker makes only when safe`() {
        val ship = SolidObject.ship(U.CENTER_OF_UNIVERSE)
        val asteroid = SolidObject.asteroid(U.CENTER_OF_UNIVERSE)
        val maker = ShipMaker(ship)
        val ignored = Transaction()
        maker.tick(U.MAKER_DELAY, ignored)
        maker.tick(0.01, ignored)
        maker.beginInteraction()
        maker.interactWithOther(asteroid)
        val nothing = maker.finishInteraction()
        assertThat(nothing.adds).isEmpty()
        assertThat(nothing.removes).isEmpty()
        maker.beginInteraction()
        // nothing
        val trans = maker.finishInteraction()
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(trans.firstRemove()).isEqualTo(maker)
        assertThat(ship.velocity).isEqualTo(Velocity.ZERO)
        assertThat(ship.heading).isEqualTo(0.0)
    }

    @Test
    fun `makes with ship features unchanged`() {
        val position = Point(123.0,456.0)
        val  velocity = Velocity(200.0,300.0)
        val heading = 37.5
        val ship = SolidObject.ship(position)
        ship.heading = heading
        ship.velocity = velocity
        val maker = ShipMaker(ship)
        maker.tick(U.MAKER_DELAY + 0.01, Transaction())
        maker.beginInteraction()
        // no obstacles
        maker.asteroidTally = 60 // no possible hyperspace failure
        val trans = maker.finishInteraction()
        assertThat(trans.adds.size).isEqualTo(2)
        assertThat(trans.adds).contains(ship)
        assertThat(ship.position).isEqualTo(position)
        assertThat(ship.velocity).isEqualTo(velocity)
        assertThat(ship.heading).isEqualTo(heading)
    }

    @Test
    fun `maker counts asteroids`() {
        val a = SolidObject.asteroid(U.randomPoint())
        val ship = SolidObject.ship(U.CENTER_OF_UNIVERSE)
        val maker = ShipMaker(ship)
        maker.beginInteraction()
        maker.interactWith(a)
        maker.interactWithOther(a)
        assertThat(maker.asteroidTally).isEqualTo(2)
    }

    @Test
    fun `hyperspace failure checks`() {
        val ship = SolidObject.ship(Point(10.0, 10.0))
        val hyper = HyperspaceOperation(ship, 0)
        assertThat(hyper.hyperspaceFailure(62, 19)).describedAs("roll 62 19 asteroids").isEqualTo(false)
        assertThat(hyper.hyperspaceFailure(62, 18)).describedAs("roll 62 18 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(45, 0)).describedAs("roll 45 0 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(44, 0)).describedAs("roll 44 0 asteroids").isEqualTo(true)
        assertThat(hyper.hyperspaceFailure(43, 0)).describedAs("roll 43 0 asteroids").isEqualTo(false)
    }
}