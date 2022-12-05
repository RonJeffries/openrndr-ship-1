package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class OneShotTest {
    @Test
    fun `runs in specified time`() {
        val trans = Transaction()
        var count = 0
        val once = OneShot(2.0) { count += 1}
        assertThat(count).isEqualTo(0)
        once.execute(trans)
        val defer = trans.firstAdd()
        defer.update(0.1, trans)
        assertThat(count).isEqualTo(0)
        defer.update(2.2, trans)
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `won't run twice immediately`() {
        val trans = Transaction()
        var count = 0
        val once = OneShot(2.0) { count += 1}
        assertThat(count).isEqualTo(0)
        once.execute(trans)
        val defer = trans.firstAdd()
        val empty = Transaction()
        once.execute(empty)
        assertThat(empty.adds).isEmpty()
    }

    @Test
    fun `will run twice after action`() {
        val trans = Transaction()
        var count = 0
        val once = OneShot(2.0) { count += 1}
        assertThat(count).isEqualTo(0)
        once.execute(trans)
        val defer = trans.firstAdd()
        val removeTrans = Transaction()
        defer.update(2.1, removeTrans)
        assertThat(count).isEqualTo(1)
        assertThat(removeTrans.firstRemove()).isEqualTo(defer)
        val another = Transaction()
        once.execute(another)
        val newDefer = another.firstAdd()
    }

    @Test
    fun `cancel removes deferred`() {
        val trans = Transaction()
        var count = 0
        val once = OneShot(2.0) { count += 1}
        assertThat(count).isEqualTo(0)
        once.execute(trans)
        val defer = trans.firstAdd()
        val remove = Transaction()
        once.cancel(remove)
        assertThat(remove.firstRemove()).isEqualTo(defer)
    }

    @Test
    fun `cancel allows another shot`() {
        val trans = Transaction()
        var count = 0
        val once = OneShot(2.0) { count += 1}
        assertThat(count).isEqualTo(0)
        once.execute(trans)
        val defer = trans.firstAdd()
        val remove = Transaction()
        once.cancel(remove)
        assertThat(remove.firstRemove()).isEqualTo(defer)
        val newOne = Transaction()
        once.execute(newOne)
        val newDefer = newOne.firstAdd()
    }
}
