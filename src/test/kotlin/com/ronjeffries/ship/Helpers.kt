package com.ronjeffries.ship

import org.assertj.core.api.Assertions
import org.openrndr.math.Vector2

fun checkVector(actual: Vector2, should: Vector2, description: String, delta: Double = 0.0001) {
    Assertions.assertThat(actual.x)
        .describedAs("$description x of (${actual.x},${actual.y})")
        .isEqualTo(should.x, Assertions.within(delta))
    Assertions.assertThat(actual.y)
        .describedAs("$description y of (${actual.x},${actual.y})")
        .isEqualTo(should.y, Assertions.within(delta))
}