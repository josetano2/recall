package edu.bluejack23_2.recall.model

import com.google.firebase.Timestamp
import edu.bluejack23_2.recall.model.supermemo.SupermemoMetadata
import java.time.LocalDate
import java.util.Date

class User(
    var id: String,
    var username: String,
    var email: String,
    var pfp: String,
    var savedDeck: List<String>,
    var playtime: Float,
    var lastOpened: Timestamp,
    var playedCards: Map<String,SupermemoMetadata>
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "username" to username,
            "email" to email,
            "pfp" to pfp,
            "savedDeck" to savedDeck,
            "playtime" to playtime,
            "lastOpened" to lastOpened,
            "playedCards" to playedCards
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): User {
            val id = map["id"] as String? ?: ""
            val username = map["username"] as String? ?: ""
            val email = map["email"] as String? ?: ""
            val pfp = map["pfp"] as String? ?: ""
            val savedDeck = map["savedDeck"] as List<String>? ?: listOf()
            val playtime = (map["playtime"] as Double)?.toFloat() ?: 0f
            val lastOpened = map["lastOpened"] as Timestamp? ?: Timestamp.now()
            val playedCards = map["playedCards"] as Map<String,SupermemoMetadata> ?: emptyMap();
            return User(id, username, email, pfp, savedDeck, playtime, lastOpened, playedCards)
        }
    }
}