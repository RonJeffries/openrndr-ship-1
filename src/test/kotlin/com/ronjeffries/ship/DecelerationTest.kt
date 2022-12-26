package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class DecelerationTest {
    @Test
    fun `print numbers`() {
        var speed = 1.0
        val speedAfterOneSecond = 0.5
        val acceleration = speedAfterOneSecond - 1
        println("  $speedAfterOneSecond")
        for (i in 0..5) {
            println("$i $speed")
            speed = speed + acceleration
        }
    }

    fun accelerationFactor(factor: Double): Double {
//        acceleration factor to accelerate to factor*currentSpeed in one second
//        Vt = V0 + at
//        factor*V0 = V0 + a*1
//        factor*V0 - V0 = a
//        V0(1 - factor) = a
        return factor - 1.0
    }

    fun accelerateToNewSpeedInOneSecond(vNew:Double, vCurrent: Double): Double {
//        vNew = vCurrent + a*t
//        t = 1
//        a = vNew - vCurrent
        return vNew - vCurrent
    }

    fun accelerateToNewSpeedInOneSecond(vNew:Velocity, vCurrent: Velocity): Velocity {
//        vNew = vCurrent + a*t
//        t = 1
//        a = vNew - vCurrent
        return vNew - vCurrent
    }

    @Test
    fun `deceleration by 25`() {
        var speed = 100.0
        val acceleration = accelerateToNewSpeedInOneSecond(75.0, 100.0)
        speed += acceleration
        assertThat(speed).isEqualTo(75.0, within(0.1))
    }

    @Test
    fun `deceleration scaled`() {
        var speed = 100.0
        val deltaTime = 0.1
        val acceleration = accelerateToNewSpeedInOneSecond(75.0, 100.0)*deltaTime
        for (i in 1..10) {
            speed += acceleration
        }
        assertThat(speed).isEqualTo(75.0, within(0.1))
    }
}