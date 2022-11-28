package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Subscriptions(
    val beforeInteractions: () -> Unit = {},

    val interactWithMissile: (missile: Missile, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithSaucer: (saucer: Saucer, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithScore: (score: Score, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithScoreKeeper: (keeper: ScoreKeeper, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShip: (ship: Ship, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShipChecker: (checker: ShipChecker, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShipDestroyer: (destroyer: ShipDestroyer, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShipMaker: (maker: ShipMaker, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithWaveMaker: (maker: WaveMaker, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithAsteroid: (asteroid: Asteroid, trans: Transaction) -> Unit = { _, _, -> },

    val afterInteractions: (trans: Transaction) -> Unit = {_ -> },

    val draw: (drawer: Drawer) -> Unit = {_ -> },
)