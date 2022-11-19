package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class TestInteraction {
    @Test
    fun `NewThing test`() {
        val thing = NewThingOne()
        val transaction = Transaction()
        val dt = 0.05
        // boilerplate calls inside cycle
        thing.events[Events.UPDATE]?.invoke(0.05, transaction)
        thing.events[Events.BEGIN_INTERACTION]?.invoke(dt,transaction)
        thing.events[Events.INTERACT_WITH]?.invoke(dt, transaction)
        thing.events[Events.END_INTERACTION]?.invoke(dt, transaction)
        assertThat(thing.elapsedTime).isEqualTo(0.05)
        assertThat(transaction.adds.size).isEqualTo(1)
        assertThat(transaction.removes.size).isEqualTo(1)
    }
}

enum class Events {
    UPDATE, BEGIN_INTERACTION, INTERACT_WITH, END_INTERACTION
}

interface EventHandler {
    val events: Map<Events, (Double, Transaction)->Unit>
}

class NewThingOne: EventHandler {
    override val events : Map<Events, (Double, Transaction)->Unit> = mapOf(
        Events.UPDATE to { dt: Double,t:Transaction -> update(dt, t) },
        Events.END_INTERACTION to { dt: Double,t:Transaction -> endInteraction(dt, t) },
    )

    var elapsedTime = 0.0

    private fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        trans.add(Score(20))
    }

    private fun endInteraction(deltaTime: Double, t: Transaction) {
        t.remove(Score(30))
    }
}
