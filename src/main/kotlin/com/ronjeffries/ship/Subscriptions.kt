package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Subscriptions(
    val beforeInteractions: () -> Unit = {},

    val interactWithAsteroid: (asteroid: Asteroid, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithMissile: (missile: Missile, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithSaucer: (saucer: Saucer, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithScore: (score: Score, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithScoreKeeper: (keeper: ScoreKeeper, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShip: (ship: Ship, trans: Transaction) -> Unit = { _, _, -> },
    val interactWithShipMaker: (maker: ShipMaker, trans: Transaction) -> Unit = { _, _, -> },

    val afterInteractions: (trans: Transaction) -> Unit = {_ -> },

    val draw: (drawer: Drawer) -> Unit = {_ -> },
    val finalize: () -> List<ISpaceObject> = { emptyList() }
)