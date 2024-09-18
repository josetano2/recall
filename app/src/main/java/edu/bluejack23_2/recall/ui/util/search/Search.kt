package edu.bluejack23_2.recall.ui.util.search

import android.util.Log
import edu.bluejack23_2.recall.model.Deck
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.ui.util.algorithm.Levenshtein

class Search {
    fun linearSearchDeck(decks: List<Deck>, query: String): List<Deck> {
        val result = mutableListOf<Deck>()
        val lowerCaseQuery = query.lowercase()

        for (deck in decks) {
            if (deck.name.lowercase().startsWith(lowerCaseQuery)) {
                result.add(deck)
            }
        }

        return result
    }


    fun fuzzySearchDeck(decks: List<Deck>, query: String): List<Deck> {
        val levenshtein = Levenshtein()
        val trackedDecks = mutableListOf<DeckDistance>()

        for (deck in decks) {
            val name = deck.name.lowercase()
            val splittedName = name.split(" ")

            var currDistance = Int.MAX_VALUE
            for (word in splittedName) {
                val distance = levenshtein.computeLevenshteinDistance(word, query.lowercase())
                if (distance < currDistance) currDistance = distance
            }

            Log.d("THIS DECK",deck.name)
            Log.d("THIS DECK DISTANCE",currDistance.toString())

            trackedDecks.add(DeckDistance(deck, currDistance))
        }

        trackedDecks.sortBy { it.distance }

        return trackedDecks.map { it.deck }
    }

    fun linearSearchUser(users: List<User>, query: String): List<User> {
        val result = mutableListOf<User>()
        val lowerCaseQuery = query.lowercase()

        for (user in users) {
            if (user.username.lowercase().startsWith(lowerCaseQuery)) {
                result.add(user)
            }
        }

        return result
    }

    fun fuzzySearchUser(users: List<User>, query: String): List<User> {
        val levenshtein = Levenshtein()
        val trackedUsers = mutableListOf<UserDistance>()

        for (user in users) {
            val name = user.username.lowercase()
            val splittedName = name.split(" ")

            var currDistance = Int.MAX_VALUE
            for (word in splittedName) {
                val distance = levenshtein.computeLevenshteinDistance(word, query.lowercase())
                if (distance < currDistance) currDistance = distance
            }

            trackedUsers.add(UserDistance(user,currDistance))
        }

        trackedUsers.sortBy { it.distance }

        return trackedUsers.map { it.user }
    }

}

data class DeckDistance(
    val deck: Deck,
    val distance: Int
)
data class UserDistance(
    val user : User,
    val distance: Int
)