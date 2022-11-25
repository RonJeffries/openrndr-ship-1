package com.ronjeffries.ship

class Interactions(
    val interactWithScore: (score: Score, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShip: (ship: Ship, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithMissile: (missile: Missile, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShipChecker: (checker: ShipChecker, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShipMaker: (maker: ShipMaker, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithWaveMaker: (maker: WaveMaker, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShipDestroyer: (destroyer: ShipDestroyer, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithAsteroid: (asteroid: Asteroid, trans: Transaction) -> Unit = { _, _, -> },
)