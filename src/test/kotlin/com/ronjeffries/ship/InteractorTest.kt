package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

interface Thing {
    fun register(dispatcher: Dispatcher)
}

interface Receiver {
    fun dispatch(other: Thing): List<Thing>
}

class TypedReceiver<THING_TYPE : Thing>(val clazz: KClass<*>, val handler: (target: THING_TYPE) -> List<Thing>) :
    Receiver {
    override fun dispatch(other: Thing): List<Thing> {
        if (clazz.isInstance(other) || clazz.isSuperclassOf(other::class)) {
            val downcast = other as THING_TYPE
            return handler(downcast)
        }
        return emptyList()
    }
}

class Dispatcher {
    val receivers = mutableListOf<Receiver>()
    fun <THING_TYPE : Thing> subscribe(clazz: KClass<THING_TYPE>, handler: (other: THING_TYPE) -> List<Thing>) {
        receivers += TypedReceiver(clazz, handler)
    }

    fun <THING_TYPE : Thing> interactWith(other: THING_TYPE): List<Thing> {
        val result = mutableListOf<Thing>()
        receivers.forEach { result += it.dispatch(other) }
        return result
    }
}

class Interactor<THING_TYPE : Thing>(val item: THING_TYPE) {

    val dispatcher = Dispatcher()

    init {
        item.register(dispatcher)
    }

    fun <THING_TYPE : Thing> interactWith(other: Interactor<THING_TYPE>): List<Thing> {
        return dispatcher.interactWith(other.item)
    }
}

class ScoreThing(val score: Int) : Thing {
    override fun register(dispatcher: Dispatcher) {
    }
}

class ScoreKeeperThing() : Thing {
    var total = 0

    override fun register(dispatcher: Dispatcher) {
        dispatcher.subscribe(ScoreThing::class, this::handle)
    }

    fun handle(score: ScoreThing): List<Thing> {
        total += score.score
        return emptyList()
    }
}


class InteractorTest {
    @Test
    fun `Score has no interactions`() {
        val score = Interactor(ScoreThing(0))
        val keeper = Interactor(ScoreKeeperThing())
        val x = score.interactWith(keeper)
        assertThat(x).isEmpty()
    }

    @Test
    fun `ScoreKeeper notices scores`() {
        val score = Interactor(ScoreThing(100))
        val keeper = Interactor(ScoreKeeperThing())
        val x = keeper.interactWith(score)
        assertThat(x).isEmpty()
        assertThat(keeper.item.total).isEqualTo(100)
    }
}