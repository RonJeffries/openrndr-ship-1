package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

private class Interactions(
    val interactWithOne: (item: ClassOne, trans: FakeTransaction) -> Unit = {_, _, -> },
    val interactWithTwo: (item: ClassTwo, trans: FakeTransaction) -> Unit= {_, _, -> },
)

private interface SO {
    val interactions: Interactions
    fun interactWith(other: SO, trans: FakeTransaction)
}

private class ClassOne: SO {
    override val interactions = Interactions(
        interactWithOne = { obj: ClassOne, trans: FakeTransaction -> trans.add("C1:C1")},
        interactWithTwo = { obj: ClassTwo, trans: FakeTransaction -> println("$this $obj"); trans.add("C1:C2")}
    )
    override fun interactWith(other: SO, trans: FakeTransaction) {
        other.interactions.interactWithOne(this, trans)
    }
}
private class ClassTwo: SO {
    override val interactions = Interactions(
        interactWithOne = { obj: ClassOne, trans -> trans.add("C2:C1") }
    )
    override fun interactWith(other: SO, trans: FakeTransaction) {
        other.interactions.interactWithTwo(this, trans)
    }
}

class FakeTransaction() {
    var messages = mutableListOf<String>()
    fun add(msg: String) {
        messages += msg
    }
}

private class InteractionTest {
    fun interactBothWays(o1: SO, o2:SO, trans: FakeTransaction) {
        o1.interactWith(o2, trans)
        o2.interactWith(o1, trans)
    }
    @Test
    fun `ClassOne sees two interactions with self`(){
        val c1 = ClassOne()
        val trans = FakeTransaction()
        interactBothWays(c1,c1,trans)
        assertThat(trans.messages).containsExactlyInAnyOrder("C1:C1", "C1:C1")
    }
    @Test
    fun `ClassTwo sees no interactions with self`(){
        val c2 = ClassTwo()
        val trans = FakeTransaction()
        interactBothWays(c2,c2,trans)
        assertThat(trans.messages.size).isEqualTo(0)
    }
    @Test
    fun `c2 c1 gets one of each`() {
        val c1 = ClassOne()
        val c2 = ClassTwo()
        val trans = FakeTransaction()
        interactBothWays(c1,c2,trans)
        assertThat(trans.messages).containsExactlyInAnyOrder("C1:C2", "C2:C1")
    }
}
