package dev.jay.gossip.documents

data class Gossip (
    val gossipId: String,
    val creatorUid: String,
    val creatorName: String,
    val gossip: String,
    val tags: List<String>,
    val time: String
        )