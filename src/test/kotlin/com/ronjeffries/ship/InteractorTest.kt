package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

interface Receiver {
    fun dispatch(other:ReactiveThing): List<ReactiveThing>
}

class TypedReceiver<T : ReactiveThing>(val clazz: KClass<*>, val handler: (announcement: T) -> List<ReactiveThing>) : Receiver {
    override fun dispatch(other: ReactiveThing): List<ReactiveThing> {
        if (clazz.isInstance(other) ) {
            val downcast = other as T
            return handler(downcast)
        }
        return emptyList()
    }
}

class Dispatcher {
    val receivers = mutableListOf<Receiver>()
    fun <T : ReactiveThing> subscribe(clazz: KClass<T>, handler: (other: T) -> List<ReactiveThing>) {
        receivers += TypedReceiver(clazz,handler)
    }

    fun <T:ReactiveThing> interactWith(other:T):List<ReactiveThing> {
        val result = mutableListOf<ReactiveThing>()
        receivers.forEach { result+=  it.dispatch(other) }
        return result
    }
}

interface Thing

interface ReactiveThing : Thing {
    fun register(dispatcher: Dispatcher)
}

class Interactor<T:ReactiveThing>(val item:T) {

    val dispatcher = Dispatcher()

    init {
        item.register(dispatcher)
    }

    fun <T:ReactiveThing> interactWith(other:Interactor<T>):List<ReactiveThing> {
        return dispatcher.interactWith(other.item)
    }
}

class ScoreThing(val score:Int) : ReactiveThing {
    override fun register(dispatcher: Dispatcher) {
    }
}

class ScoreKeeperThing() : ReactiveThing {
    var total = 0

    override fun register(dispatcher: Dispatcher) {
        dispatcher.subscribe(ScoreThing::class,this::handle)
    }

    fun handle(score:ScoreThing) : List<ReactiveThing> {
        total+=score.score
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