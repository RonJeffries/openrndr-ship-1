package com.ronjeffries.ship

class Interactions (
    val interactWithScore: (score: Score, trans: Transaction) -> Unit = { _,_, -> },
    val interactWithSolidObject: (solid: SolidObject, trans: Transaction) -> Unit = { _,_, -> },
)