package edu.bluejack23_2.recall.model

class Deck(
    var id: String,
    var creatorId : String,
    var name: String,
    var cards: List<Card>
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "creatorId" to creatorId,
            "name" to name,
            "cards" to cards.map { it.toMap() }
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Deck {
            val id = map["id"] as? String ?: ""
            var creatorId = map["creatorId"] as? String ?: ""
            val name = map["name"] as? String ?: ""
            val cardsMapList = map["cards"] as? List<Map<String, Any>> ?: emptyList()
            val cards = cardsMapList.map { Card.fromMap(it) }
            return Deck(id, creatorId, name, cards)
        }
    }
}
