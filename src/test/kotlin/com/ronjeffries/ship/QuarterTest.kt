package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class QuarterTest {
    val controls = Controls()
    val quarter = Quarter(controls)
    val trans = Transaction()
    @Test
    fun `create it`() {
        assertThat(quarter).isNotNull
    }

    @Test
    fun `clears all objects`() {
        quarter.update(0.0, trans)
        assertThat(trans.shouldClear).isEqualTo(true)
    }

    @Test
    fun `adds ScoreKeeper`() {
        quarter.update(0.0, trans)
        assertThat(trans.adds.any { it is ScoreKeeper }).isEqualTo(true)
    }

    @Test
    fun `adds WaveMaker`() {
        quarter.update(0.0, trans)
        assertThat(trans.adds.any { it is WaveMaker }).isEqualTo(true)
    }

    @Test
    fun `adds SaucerMaker`() {
        quarter.update(0.0, trans)
        assertThat(trans.adds.any { it is SaucerMaker }).isEqualTo(true)
    }

    @Test
    fun `adds correctly configured ShipChecker`() {
        quarter.update(0.0, trans)
        assertThat(trans.adds.any { it is ShipChecker }).isEqualTo(true)
        val checker = trans.adds.first { it is ShipChecker } as ShipChecker
        assertThat(checker.scoreKeeper is ScoreKeeper).isEqualTo(true)
        val ship = checker.ship
        assertThat(ship.controls).isEqualTo(controls)
    }
}