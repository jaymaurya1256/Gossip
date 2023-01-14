package dev.jay.gossip.documents

data class Gossip (
    val creatorName: String,
    val gossip: String,
    val tags: List<String>,
    val time: String
        )