package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


interface Thing {
    val dispatch: Dispatch
    fun callOther(other: Thing, accumulator: Accumulator)
}

class TypeOne : Thing {
    override val dispatch: Dispatch = Dispatch(
        handleTypeOne = this::handleTypeOne,
        handleTypeTwo = this::handleTypeTwo
    )

    override fun callOther(other: Thing, accumulator: Accumulator) = other.dispatch.handleTypeOne(this, accumulator)

    fun handleTypeOne(thing: TypeOne, accumulator: Accumulator) {
        accumulator.add("${this::class.simpleName}:${thing::class.simpleName}")
    }

    fun handleTypeTwo(thing: TypeTwo, accumulator: Accumulator) {
        accumulator.add("${this::class.simpleName}:${thing::class.simpleName}")
    }
}

class TypeTwo : Thing {
    override val dispatch: Dispatch = Dispatch(
        handleTypeOne = this::handleTypeOne,
        handleTypeTwo = this::handleTypeTwo
    )

    override fun callOther(other: Thing, accumulator: Accumulator) = other.dispatch.handleTypeTwo(this, accumulator)

    fun handleTypeOne(thing: TypeOne, accumulator: Accumulator) {
        accumulator.add("${this::class.simpleName}:${thing::class.simpleName}")
    }

    fun handleTypeTwo(thing: TypeTwo, accumulator: Accumulator) {
        accumulator.add("${this::class.simpleName}:${thing::class.simpleName}")
    }
}

class OnlyLikesOnes() : Thing {
    override val dispatch: Dispatch = Dispatch(
        handleTypeOne = this::handleTypeOne,
    )

    override fun callOther(other: Thing, accumulator: Accumulator) =
        other.dispatch.handleOnlyLikesOnes(this, accumulator)

    fun handleTypeOne(thing: TypeOne, accumulator: Accumulator) {
        accumulator.add("${this::class.simpleName}:${thing::class.simpleName}")
    }

}

class Accumulator() {
    val texts = mutableListOf<String>()
    fun add(text: String) {
        texts.add(text)
    }
}

class Dispatch(
    val handleTypeOne: (thing: TypeOne, accumulator: Accumulator) -> Unit = { _, _ -> },
    val handleTypeTwo: (thing: TypeTwo, accumulator: Accumulator) -> Unit = { _, _ -> },
    val handleOnlyLikesOnes: (thing: OnlyLikesOnes, accumulator: Accumulator) -> Unit = { _, _ -> }
)

class TypedCallbackTest {
    fun interact(first: Thing, second: Thing, accumulator: Accumulator) {
        first.callOther(second, accumulator)
        second.callOther(first, accumulator)
    }

    @Test
    fun `pair of differents`() {
        val accumulator = Accumulator()
        interact(TypeOne(), TypeTwo(), accumulator)
        assertThat(accumulator.texts).containsExactlyInAnyOrder("TypeOne:TypeTwo", "TypeTwo:TypeOne")
    }

    @Test
    fun `pair of ones`() {
        val accumulator = Accumulator()
        interact(TypeOne(), TypeOne(), accumulator)
        assertThat(accumulator.texts).containsExactly("TypeOne:TypeOne", "TypeOne:TypeOne")
    }

    @Test
    fun `only likes ones sees one`() {
        val accumulator = Accumulator()
        interact(OnlyLikesOnes(), TypeOne(), accumulator)
        assertThat(accumulator.texts).containsExactly("OnlyLikesOnes:TypeOne")
    }

    @Test
    fun `only likes ones sees two`() {
        val accumulator = Accumulator()
        interact(OnlyLikesOnes(), TypeTwo(), accumulator)
        assertThat(accumulator.texts).isEmpty()
    }

}