package dev.jay.gossip.documents

data class Message (
    val message: String = "",
    val name: String = "",
    val uid: String = ""
    ) {
    var id : String = ""
}