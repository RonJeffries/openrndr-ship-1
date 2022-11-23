package com.ronjeffries.ship

class Interactions (
    val interactWithScore: (score: Score, trans: Transaction) -> Unit = { _,_, -> }
)