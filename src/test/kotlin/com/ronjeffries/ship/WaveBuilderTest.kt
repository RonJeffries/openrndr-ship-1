package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WaveBuilderTest {
    val builder = WaveBuilder()
    val transaction = Transaction()

    @Test
    fun `no change if there are asteroids`() {
        builder.update(1.0, 1, transaction)
        assertThat(transaction.typedAdds.all).isEmpty()
        assertThat(transaction.typedRemoves.all).isEmpty()
    }

    @Test
    fun `no change if no asteroids on first call`() {
        builder.update(1.0, 0, transaction)
        assertThat(transaction.typedAdds.all).isEmpty()
        assertThat(transaction.typedRemoves.all).isEmpty()
    }

    @Test
    fun `no change if no asteroids on call within delay`() {
        builder.update(1.0, 0, transaction)
        builder.update(2.0, 0, transaction)
        assertThat(transaction.typedAdds.all).isEmpty()
        assertThat(transaction.typedRemoves.all).isEmpty()
    }

    @Test
    fun `asteroids added on call outside delay`() {
        builder.update(1.0, 0, transaction)
        builder.update(3.1, 0, transaction)
        assertThat(transaction.typedAdds.asteroids.size).isEqualTo(4)
        assertThat(transaction.typedRemoves.all).isEmpty()
    }


}