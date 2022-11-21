package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScoreKeeperTest {
    @Test
    fun `scorekeeper starts low`() {
        val keeper = ScoreKeeper()
        assertThat(keeper.totalScore).isEqualTo(0)
    }

    @Test
    fun `scorekeeper formats interestingly`() {
        val keeper = ScoreKeeper()
        keeper.totalScore = 123
        assertThat(keeper.formatted()).isEqualTo("00123")
    }

    @Test
    fun `scorekeeper captures keeper vs other`() {
        val score = Score(20)
        val keeper = ScoreKeeper()
        val discards = keeper.interactions.interactWith(score)
        assertThat(discards.size).isEqualTo(1)
        assertThat(discards).contains(score)
        assertThat(keeper.formatted()).isEqualTo("00020")
    }
}