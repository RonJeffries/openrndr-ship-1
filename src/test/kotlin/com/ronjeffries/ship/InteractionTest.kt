package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

interface SO {
    fun interactWith(other: SO, trans: FakeTransaction)
    fun interactWithOne(classOne: ClassOne, trans: FakeTransaction)
    fun interactWithTwo(classTwo: ClassTwo, trans: FakeTransaction)
}
class ClassOne: SO {
    override fun interactWith(other: SO, trans: FakeTransaction) {
        other.interactWithOne(this, trans)
    }
    override fun interactWithOne(classOne: ClassOne, trans: FakeTransaction) {
        trans.add("C1:C1 ")
    }
    override fun interactWithTwo(classTwo: ClassTwo, trans: FakeTransaction) {
        trans.add("C1:C2 ")
    }
}
class ClassTwo: SO {
    override fun interactWith(other: SO, trans: FakeTransaction) {
        other.interactWithTwo(this, trans)
    }
    override fun interactWithOne(classOne: ClassOne, trans: FakeTransaction) {
        trans.add("C2:C1 " )
    }
    override fun interactWithTwo(classTwo: ClassTwo, trans: FakeTransaction){
    }
}

class FakeTransaction() {
    var message = ""
    fun add(msg: String) {
        message += msg
    }
}

class InteractionTest {
    fun interactBothWays(o1: SO, o2:SO, trans: FakeTransaction) {
        o1.interactWith(o2, trans)
        o2.interactWith(o1, trans)
    }
    @Test
    fun `ClassOne sees two interactions with self`(){
        val c1 = ClassOne()
        val trans = FakeTransaction()
        interactBothWays(c1,c1,trans)
        assertThat(trans.message).isEqualTo("C1:C1 C1:C1 ")
    }
    @Test
    fun `ClassTwo sees no interactions with self`(){
        val c2 = ClassTwo()
        val trans = FakeTransaction()
        interactBothWays(c2,c2,trans)
        assertThat(trans.message).isEqualTo("")
    }
    @Test
    fun `c2 c1 gets one of each`() {
        val c1 = ClassOne()
        val c2 = ClassTwo()
        val trans = FakeTransaction()
        interactBothWays(c1,c2,trans)
        assertThat(trans.message).contains("C1:C2 ")
        assertThat(trans.message).contains("C2:C1 ")
    }
}