package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/*
Marker interface. Anything that's a message can be sent across the wire.
 */
interface Message

/*
Things are all the things currently derived from SpaceObject. They will all have to implment
register. There is no further class hierarchy requirement.
 */
interface Thing {
    fun register(dispatcher: Dispatcher)
}

/*
This fills the role currently held by Transaction. Add any API you want. For POC, it just collects texts.
 */
class Accumulator {
    val texts = mutableListOf<String>()

    fun add(text: String) {
        texts.add(text)
    }
}

interface Receiver {
    fun dispatch(message: Message, accumulator: Accumulator)
}

class TypedReceiver<MESSAGE_TYPE : Message>(
    val clazz: KClass<*>,
    val handler: (message: MESSAGE_TYPE, accumulator: Accumulator) -> Unit
) :
    Receiver {
    override fun dispatch(message: Message, accumulator: Accumulator) {
        if (clazz.isInstance(message) || clazz.isSuperclassOf(message::class)) {
            val downcast = message as MESSAGE_TYPE
            handler(downcast, accumulator)
        }
    }
}

class Dispatcher {
    val receivers = mutableListOf<Receiver>()
    fun <MESSAGE_TYPE : Message> subscribe(
        clazz: KClass<MESSAGE_TYPE>,
        handler: (other: MESSAGE_TYPE, accumulator: Accumulator) -> Unit
    ) {
        receivers += TypedReceiver(clazz, handler)
    }

    fun <MESSAGE_TYPE : Message> receive(message: MESSAGE_TYPE, accumulator: Accumulator) {
        receivers.forEach { it.dispatch(message, accumulator) }
    }
}

class Interactor<THING_TYPE : Thing>(val item: THING_TYPE) {

    val dispatcher = Dispatcher()

    init {
        item.register(dispatcher)
    }

    fun receive(message: Message, accumulator: Accumulator) {
        dispatcher.receive(message, accumulator)
    }
}

class Smalltalk {

    val objects = mutableListOf<Interactor<*>>()
    val thingToInteractor = mutableMapOf<Thing, Interactor<*>>()

    fun sendAll(message: Message, accumulator: Accumulator) {
        objects.forEach { it.receive(message, accumulator) }
    }

    fun add(thing: Thing) {
        val interactor = Interactor(thing)
        objects.add(interactor)
        thingToInteractor.put(thing, interactor)
    }

    fun send(thing: Thing, message: Message, accumulator: Accumulator) {
        val interactor = thingToInteractor.get(thing)
        interactor?.receive(message, accumulator)
    }
}

data class DrawMessage(val drawer: Int) : Message
data class ScoreMessage(val score: Int) : Message

class ScoreThing(val score: Int) : Thing {
    override fun register(dispatcher: Dispatcher) {
    }
}

class ScoreKeeperThing() : Thing {
    var total = 0

    override fun register(dispatcher: Dispatcher) {
        dispatcher.subscribe(ScoreMessage::class, this::score)
        dispatcher.subscribe(DrawMessage::class) { message, accumulator ->
            accumulator.add("ScoreKeeperThing drew!")
        }
    }

    fun score(message: ScoreMessage, accumulator: Accumulator) {
        accumulator.add("ScoreKeeperThing got a score ${message.score}.")
        total += message.score
    }
}


class SmalltalkTest {

    val smalltalk = Smalltalk()
    val accumulator = Accumulator()

    @Test
    fun `Send a draw message to everyone`() {
        smalltalk.add(ScoreKeeperThing())
        smalltalk.add(ScoreThing(100))
        smalltalk.sendAll(DrawMessage(1), accumulator)
        // only ScoreKeeper is interested in drawing.
        assertThat(accumulator.texts).containsExactly("ScoreKeeperThing drew!")
    }

    @Test
    fun `Send a score message just to the scorekeeper`() {
        val scoreKeeper = ScoreKeeperThing()
        smalltalk.add(scoreKeeper)

        smalltalk.send(scoreKeeper, ScoreMessage(100), accumulator)
        assertThat(accumulator.texts).containsExactly("ScoreKeeperThing got a score 100.")
    }
}