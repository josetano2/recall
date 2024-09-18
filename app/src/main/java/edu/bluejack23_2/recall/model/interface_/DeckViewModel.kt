package edu.bluejack23_2.recall.model.interface_

interface DeckViewModel {
    fun getDecks()
    suspend fun getUsername(id: String): String
    fun performLinearSearch(query: String, type : String?)
    fun performFuzzySearch(query: String, type : String?)
    fun addDeckToCurrentUser(deckId : String)
    fun removeDeckFromCurrentUser(deckId : String)
}
