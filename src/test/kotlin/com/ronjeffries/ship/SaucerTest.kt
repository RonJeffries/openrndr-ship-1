package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SaucerTest {
    @Test
    fun `created on edge`() {
        val saucer = Saucer()
        assertThat(saucer.position.x).isEqualTo(0.0)
    }

    @Test
    fun `starts left-right`() {
        val saucer = Saucer()
        saucer.wakeUp()
        assertThat(saucer.velocity.x).isGreaterThan(0.0)
        assertThat(saucer.velocity.y).isEqualTo(0.0)
    }

    @Test
    fun `direction changes`() {
        var dir: Velocity
        val saucer = Saucer()
        saucer.wakeUp()
        dir = saucer.newDirection(1)
        assertThat(dir.x).isEqualTo(0.7071, within(0.0001))
        assertThat(dir.y).isEqualTo(0.7071, within(0.0001))
        dir = saucer.newDirection(2)
        assertThat(dir.x).isEqualTo(0.7071, within(0.0001))
        assertThat(dir.y).isEqualTo(-0.7071, within(0.0001))
        dir = saucer.newDirection(0)
        assertThat(dir.x).isEqualTo(1.0, within(0.0001))
        assertThat(dir.y).isEqualTo(0.0, within(0.0001))
    }

    @Test
    fun `saucer asteroid collision`() {
        val saucer = Saucer()
        saucer.position = Point(249.0, 0.0)
        val asteroid = Asteroid(Point.ZERO)
        val trans = Transaction()
        saucer.subscriptions.interactWithAsteroid(asteroid, trans)
        asteroid.subscriptions.interactWithSaucer(saucer, trans)
        assertThat(trans.removes).contains(asteroid)
        assertThat(trans.removes).contains(saucer)
    }

    @Test
    fun `missile asteroid collision`() {
        val asteroid = Asteroid(Point.ZERO)
        asteroid.position = Point(249.0, 0.0)
        val missile = Missile(Point.ZERO)
        missile.position = asteroid.position
        val trans = Transaction()
        missile.subscriptions.interactWithAsteroid(asteroid, trans)
        assertThat(trans.removes).contains(missile)
        asteroid.subscriptions.interactWithMissile(missile, trans)
        assertThat(trans.removes).contains(asteroid)
    }

    @Test
    fun `asteroid asteroid collision`() {
        val asteroid = Asteroid(Point.ZERO)
        asteroid.position = Point(249.0, 0.0)
        val asteroid2 = Asteroid(Point.ZERO)
        asteroid2.position = asteroid.position
        val trans = Transaction()
        asteroid2.subscriptions.interactWithAsteroid(asteroid, trans)
        asteroid.subscriptions.interactWithAsteroid(asteroid2, trans)
        assertThat(trans.removes).isEmpty()
    }

}