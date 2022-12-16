package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
import org.openrndr.math.asRadians
import kotlin.math.cos
import kotlin.math.sin


class SolidObjectTest {
    private val tick = 1.0/60.0

    @Test
    fun `Ship Happens`() {
        val ship = Ship(
            position = Vector2.ZERO
        )
        ship.velocity = Vector2(120.0, 120.0)
        ship.update(tick, Transaction())
        assertThat(ship.position).isEqualTo(Vector2(2.0, 2.0))
    }

    @Test
    fun `capping works high`() {
        val ship = Ship(
            position = Vector2(U.UNIVERSE_SIZE - 1, U.UNIVERSE_SIZE / 2)
        )
        ship.velocity = Vector2(120.0, 120.0)
        ship.update(tick, Transaction())
        assertThat(ship.position.x).isEqualTo(1.0)
        assertThat(ship.position.y).isEqualTo(U.UNIVERSE_SIZE / 2 + 2)
    }

    @Test
    fun `capping works low`() {
        val ship = Ship(
            position = Vector2(1.0, U.UNIVERSE_SIZE / 2)
        )
        ship.velocity = Vector2(-120.0, -120.0)
        ship.update(tick, Transaction())
        assertThat(ship.position.x).isEqualTo(U.UNIVERSE_SIZE - 1)
        assertThat(ship.position.y).isEqualTo(U.UNIVERSE_SIZE / 2 - 2)
    }

    @Test
    fun `acceleration works`() {
        val control = Controls()
        val ship = Ship(
            position = Vector2.ZERO,
            controls = control
        )
        assertThat(ship.position).isEqualTo(Vector2.ZERO)
        assertThat(ship.velocity).isEqualTo(Vector2.ZERO)
        ship.update(tick, Transaction())
        assertThat(ship.position).isEqualTo(Vector2.ZERO)
        assertThat(ship.velocity).isEqualTo(Vector2.ZERO)
        control.accelerate = true
        ship.update(tick, Transaction())
        val deltaXPerSecond = U.SHIP_ACCELERATION.x
        checkVector(ship.velocity, Vector2.UNIT_X * deltaXPerSecond / 60.0, "velocity")
        checkVector(ship.position, Vector2(deltaXPerSecond / 60.0 / 60.0, 0.0), "position")
    }

    @Test
    fun `ship can turn left`() {
        val control = Controls()
        val ship = Ship(
            position = Vector2.ZERO,
            controls = control
        )
        control.left = true
        ship.update(tick * 30, Transaction())
        val expected = U.SHIP_ROTATION_SPEED * 30.0 / 60.0
        assertThat(ship.heading).isEqualTo(-expected, within(0.01))
        control.left = false
        control.accelerate = true
        val expectedVelocity = U.SHIP_ACCELERATION.rotate(-expected)
        ship.update(tick * 60, Transaction())
        checkVector(ship.velocity, expectedVelocity, "rotated velocity")
    }

    @Test
    fun `ship can turn right`() {
        val control = Controls()
        val ship = Ship(
            position = Vector2.ZERO,
            controls = control
        )
        control.right = true
        ship.update(tick * 10, Transaction())
        val expected = U.SHIP_ROTATION_SPEED * 10.0 / 60.0
        assertThat(ship.heading).isEqualTo(expected, within(0.01))
    }

    @Test
    fun `speed of light`() {
        val control = Controls()
        val ship = Ship(
            position = Vector2.ZERO,
            controls = control
        )
        ship.heading = -60.0 // northeast ish
        control.accelerate = true
        ship.update(100.0, Transaction()) // long time
        val v = ship.velocity
        val speed = v.length
        assertThat(speed).isEqualTo(U.SPEED_OF_LIGHT, within(1.0))
        val radians60 = 60.0.asRadians
        val expected = Vector2(cos(radians60), -sin(radians60)) * U.SPEED_OF_LIGHT
        checkVector(v, expected, "velocity", 1.0)
    }

    @Test
    fun `ship can fire missile`() {
        val controls = Controls()
        val ship = Ship(
            position = Vector2.ZERO,
            controls = controls
        )
        controls.fire = true
        val newMissiles = Transaction()
        ship.update(tick, newMissiles)
        assertThat(newMissiles.adds.size).isEqualTo(1) // does not return itself
    }

    @Test
    fun `can only fire once per press`() {
        val controls = Controls()
        val ship = Ship(
            position = Vector2.ZERO,
            controls = controls
        )
        controls.fire = true
        val oneMissile = Transaction()
        ship.update(tick, oneMissile)
        assertThat(oneMissile.adds.size).isEqualTo(1)
        val noMissiles = Transaction()
        ship.update(tick, noMissiles)
        assertThat(noMissiles.adds.size).isEqualTo(0) // no firing
        controls.fire = false
        val newMissiles = Transaction()
        ship.update(tick, newMissiles)
        assertThat(newMissiles.adds.size).isEqualTo(0)
        controls.fire = true
        ship.update(tick, newMissiles)
        assertThat(newMissiles.adds.size).isEqualTo(1)
    }

    @Test
    fun `collision ideas`() {
        var x = 0
        val objects = listOf(1,2,3)
        for (first in objects) for (second in objects) {x++}
        for (i in 1..objects.size) for (j in i..objects.size) {x++}
    }
//
//    @Test
//    fun `collision INDEXING test`() {
//        val p1 = Vector2(100.0,100.0)
//        val p2 = Vector2(755.0, 500.0)
//        val a0 = SolidObject.asteroid(p1) // yes
//        val m1 = SolidObject(p1, Velocity.ZERO, 10.0) // yes
//        val s2 = SolidObject.ship(p1) // yes
//        val a3 = SolidObject.asteroid(p2) // no
//        val a4 = SolidObject.asteroid(p2) // no
//        val objects: MutableList<ISpaceObject> = mutableListOf(a0,m1,s2, a3,a4)
//        val shouldDie: MutableSet<ISpaceObject> = mutableSetOf<ISpaceObject>()
//        var ct = 0
//        for (i in 0 until objects.size-1) {
//            for (j in i+1 until objects.size) {
//                ct += 1
//                val oi = objects[i]
//                val oj = objects[j]
//                shouldDie.addAll(oi.interactWith(oj))
//            }
//        }
//        val n = objects.size
//        assertThat(ct).isEqualTo(n*(n-1)/2)
//        assertThat(shouldDie.size).isEqualTo(3)
//    }

//    @Test
//    fun `collision PLAIN LOOP test better`() {
//        val p1 = Vector2(100.0,100.0)
//        val p2 = Vector2(750.0, 500.0)
//        val v = Vector2.ZERO
//        val a0 = SolidObject.asteroid(p1,v) // yes
//        val m1 = SolidObject(p1, v, 10.0) // yes
//        val s2 = SolidObject.ship(p1) // yes
//        val a3 = SolidObject.asteroid(p2,v) // no
//        val a4 = SolidObject.asteroid(p2,v) // no
//        val objects = mutableListOf(a0,m1,s2, a3,a4)
//        val shouldDie = mutableSetOf<ISpaceObject>()
//        var ct = 0
//        for (oi in objects) {
//            for (oj in objects) {
//                ct += 1
//                shouldDie.addAll(oi.interactWith(oj))
//            }
//        }
//        val n = objects.size
//        assertThat(ct).isEqualTo(n*n)
//        assertThat(shouldDie.size).isEqualTo(3)
//    }

    @Test
    fun `missile starts ahead of ship`() {
        val sixtieth = 1.0 / 60.0
        val controls = Controls()
        val ship = Ship(
            position = Vector2(1000.0, 1000.0),
            controls = controls
        )
        ship.heading = 0.0
        controls.fire = true

        // hand calculate expected result
        val missileOffset = Vector2(2 * U.KILL_SHIP + 2 * U.KILL_MISSILE, 0.0)
        val expectedPosition = ship.position + missileOffset.rotate(ship.heading)

        // fire missile and check it
        val additions = Transaction()
        ship.update(sixtieth, additions)
        val missile = additions.firstAdd() as Missile
        assertThat(missile.position).isEqualTo(expectedPosition)
    }

    @Test
    fun `missile starts ahead of ship when rotated`() {
        val sixtieth = 1.0 / 60.0
        val controls = Controls()
        val ship = Ship(
            position = Vector2(1000.0, 1000.0),
            controls = controls
        )

        // hand calculate expected result
        val missileOffset = Vector2(2 * U.KILL_SHIP + 2 * U.KILL_MISSILE, 0.0)

        // check that we always position the missile in front of the ship
        ship.heading = 90.0
        controls.fire = true
        val expectedPosition = ship.position + missileOffset.rotate(ship.heading)
        val additions = Transaction()
        ship.update(sixtieth, additions)
        val missile = additions.firstAdd() as Missile
        assertThat(missile.position).isEqualTo(expectedPosition)
    }

    @Test
    fun `does not fire if fire flag not set`() {
        val sixtieth = 1.0 / 60.0
        val controls = Controls()
        val ship = Ship(
            position = Vector2(1000.0, 1000.0),
            controls = controls
        )
        controls.fire = false
        val noMissile = Transaction()
        ship.update(sixtieth, noMissile)
        assertThat(noMissile.adds).isEmpty()
    }
}

fun checkVector(actual:Vector2, should: Vector2, description: String, delta: Double = 0.0001) {
    assertThat(actual.x)
        .describedAs("$description x of (${actual.x},${actual.y})")
        .isEqualTo(should.x, within(delta))
    assertThat(actual.y)
        .describedAs("$description y of (${actual.x},${actual.y})")
        .isEqualTo(should.y, within(delta))
}