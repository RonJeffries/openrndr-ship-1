package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class MissileTest {
    @Test
    fun `can be created`() {
        val ship = SolidObject.ship(U.randomPoint())
        val missile = Missile(ship)
    }
}