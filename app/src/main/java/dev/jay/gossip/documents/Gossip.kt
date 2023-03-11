package dev.jay.gossip.documents

data class Gossip (
    val creatorName: String = "",
    val gossip: String = "",
    val tags: List<String> = listOf(),
    val time: Long = 0L,
    val uid: String = ""
) {
    var id: String = ""
}