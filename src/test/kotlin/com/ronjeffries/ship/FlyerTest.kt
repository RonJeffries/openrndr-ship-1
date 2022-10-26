package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2
import java.lang.Math.*


class FlyerTest {
    private val tick = 1.0/60.0
    @Test
    fun `Ship Happens`() {
        val ship = Flyer.ship(Vector2.ZERO)
        ship.velocity = Vector2(120.0,120.0)
        ship.update(tick)
        assertThat(ship.position).isEqualTo(Vector2(2.0,2.0))
    }

    @Test
    fun `capping works high`() {
        val ship = Flyer.ship(Vector2(9999.0, 5000.0))
        ship.velocity = Vector2(120.0,120.0)
        ship.update(tick)
        assertThat(ship.position.x).isEqualTo(1.0)
        assertThat(ship.position.y).isEqualTo(5002.0)
    }

    @Test
    fun `capping works low`() {
        val ship = Flyer.ship( Vector2(1.0, 5000.0))
        ship.velocity = Vector2(-120.0, -120.0)
        ship.update(tick)
        assertThat(ship.position.x).isEqualTo(9999.0)
        assertThat(ship.position.y).isEqualTo(4998.0)
    }

    @Test
    fun `acceleration works`() {
        val control = Controls()
        val ship = Flyer.ship(Vector2.ZERO, control)
        assertThat(ship.position).isEqualTo(Vector2.ZERO)
        assertThat(ship.velocity).isEqualTo(Vector2.ZERO)
        ship.update(tick)
        assertThat(ship.position).isEqualTo(Vector2.ZERO)
        assertThat(ship.velocity).isEqualTo(Vector2.ZERO)
        control.accelerate = true
        ship.update(tick)
        checkVector(ship.velocity, Vector2.UNIT_X, "velocity")
        checkVector(ship.position, Vector2(1.0/60.0, 0.0), "position")
    }

    @Test
    fun `ship can turn left`() {
        val control = Controls()
        val ship = Flyer.ship(Vector2.ZERO, control)
        control.left = true
        ship.update(tick*15)
        assertThat(ship.heading).isEqualTo(90.0, within(0.01))
        control.left = false
        control.accelerate  = true
        ship.update(tick*60)
        checkVector(ship.velocity, Vector2(0.0,60.0), "rotated velocity")
    }

    @Test
    fun `ship can turn right`() {
        val control = Controls()
        val ship = Flyer.ship(Vector2.ZERO, control)
        control.right = true
        ship.update(tick*10)
        assertThat(ship.heading).isEqualTo(-60.0, within(0.01))
    }

    @Test
    fun `speed of light`() {
        val control = Controls()
        val ship = Flyer.ship(Vector2.ZERO, control)
        control.left = true
        ship.update(tick*10) // 60 degrees north east ish
        control.left = false
        control.accelerate = true
        ship.update(100.0) // long time
        val v = ship.velocity
        val speed = v.length
        assertThat(speed).isEqualTo(5000.0, within(1.0))
        val radians60 = toRadians(60.0)
        val expected = Vector2(cos(radians60), sin(radians60))*5000.0
        checkVector(v, expected, "velocity", 1.0)
    }

    @Test
    fun `ship can fire missile`() {
        val controls = Controls()
        val ship = Flyer.ship(Vector2.ZERO, controls)
        controls.fire = true
        val flyers = ship.update(tick)
        assertThat(flyers.size).isEqualTo(2)
    }

    @Test
    fun `can only fire once per press`() {
        val controls = Controls()
        val ship = Flyer.ship(Vector2.ZERO, controls)
        controls.fire = true
        var flyers = ship.update(tick)
        assertThat(flyers.size).isEqualTo(2)
        flyers = ship.update(tick)
        assertThat(flyers.size).isEqualTo(1)
        controls.fire = false
        flyers = ship.update(tick)
        assertThat(flyers.size).isEqualTo(1)
        controls.fire = true
        flyers = ship.update(tick)
        assertThat(flyers.size).isEqualTo(2)
    }

    @Test
    fun `collision ideas`() {
        var x = 0
        val objects = listOf(1,2,3)
        for (first in objects) for (second in objects) {x++}
        for (i in 1..objects.size) for (j in i..objects.size) {x++}
    }

    @Test
    fun `collision INDEXING test`() {
        val p1 = Vector2(100.0,100.0)
        val p2 = Vector2(755.0, 500.0)
        val v = Vector2.ZERO
        val a0 = Flyer.asteroid(p1,v) // yes
        val m1 = Flyer(p1, v, 10.0) // yes
        val s2 = Flyer.ship(p1) // yes
        val a3 = Flyer.asteroid(p2,v) // no
        val a4 = Flyer.asteroid(p2,v) // no
        val objects = mutableListOf<Flyer>(a0,m1,s2, a3,a4)
        val shouldDie = mutableSetOf<Flyer>()
        var ct = 0
        for (i in 0 until objects.size-1) {
            for (j in i+1 until objects.size) {
                ct = ct + 1
                val oi = objects[i]
                val oj = objects[j]
                if (oi.collides(oj)) {
                    shouldDie.add(objects[i])
                    shouldDie.add(objects[j])
                }
            }
        }
        val n = objects.size
        assertThat(ct).isEqualTo(n*(n-1)/2)
        assertThat(shouldDie.size).isEqualTo(3)
    }

    @Test
    fun `collision PLAIN LOOP test better`() {
        val p1 = Vector2(100.0,100.0)
        val p2 = Vector2(750.0, 500.0)
        val v = Vector2.ZERO
        val a0 = Flyer.asteroid(p1,v) // yes
        val m1 = Flyer(p1, v, 10.0) // yes
        val s2 = Flyer.ship(p1) // yes
        val a3 = Flyer.asteroid(p2,v) // no
        val a4 = Flyer.asteroid(p2,v) // no
        val objects = mutableListOf<Flyer>(a0,m1,s2, a3,a4)
        val shouldDie = mutableSetOf<Flyer>()
        var ct = 0
        for (oi in objects) {
            for (oj in objects) {
                ct += 1
                if (oi.collides(oj)) {
                    shouldDie.add(oi)
                    shouldDie.add(oj)
                }
            }
        }
        val n = objects.size
        assertThat(ct).isEqualTo(n*n)
        assertThat(shouldDie.size).isEqualTo(3)
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