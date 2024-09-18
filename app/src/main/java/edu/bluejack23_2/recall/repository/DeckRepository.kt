package edu.bluejack23_2.recall.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.recall.model.Card
import edu.bluejack23_2.recall.model.Deck
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class DeckRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val userRepository: UserRepository = UserRepository()

    fun addDeck(deck: Deck) {
        val deckMap = deck.toMap()
        Log.d("Deck", deckMap.toString())

        firestore.collection("decks").add(deckMap).addOnSuccessListener { docRef ->
            val deckId = docRef.id
            docRef.update("id", deckId).addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    addDeckToCurrentUser(deckId)
                }
            }.addOnFailureListener { e ->
                Log.e("DeckRepository", "Error updating deck ID: ${e.message}", e)
            }
        }.addOnFailureListener { e ->
            Log.e("DeckRepository", "Error adding deck: ${e.message}", e)
        }
    }

    suspend fun getDecks(): List<Deck> {
        val deckList = mutableListOf<Deck>()

        try {
            val querySnapshot = firestore.collection("decks").get().await()

            for (doc in querySnapshot.documents) {
                val map = doc.data
                if (map != null) {
                    val deck = Deck.fromMap(map)
                    deckList.add(deck)
                }
            }
        } catch (e: Exception) {
            Log.e("DeckRepository", "Error getting decks: ${e.message}", e)
        }

        return deckList
    }

    suspend fun addDeckToCurrentUser(deckId: String) {
        val user = userRepository.getUser()

        if (user != null) {
            val userId = user.id

            val querySnapshot = firestore.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val userDoc = querySnapshot.documents[0]
                val userRef = userDoc.reference

                val currentDecks =
                    userDoc.get("savedDeck") as? MutableList<String> ?: mutableListOf()

                if (!currentDecks.contains(deckId)) {
                    currentDecks.add(deckId)
                }
                userRef.update("savedDeck", currentDecks).await()
            }
        }
    }

    suspend fun getDeckById(deckId: String): Deck? {
        val querySnapshot = firestore.collection("decks")
            .whereEqualTo("id", deckId)
            .get()
            .await()

        if (querySnapshot.isEmpty) return null

        val obj = querySnapshot.documents[0].data ?: return null
        return Deck.fromMap(obj as Map<String, Any>)
    }


    suspend fun getUserDeck(): List<Deck> = coroutineScope {
        val deckList = mutableListOf<Deck>()

        try {
            val user = userRepository.getUser() ?: return@coroutineScope emptyList()
            val userId = user.id

            val querySnapshot = firestore.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val userDoc = querySnapshot.documents[0]
                val currentDecks =
                    userDoc.get("savedDeck") as? MutableList<String> ?: mutableListOf()

                val deckDeferred = currentDecks.map { deckId ->
                    async { getDeckById(deckId) }
                }

                val decks = deckDeferred.awaitAll()


                deckList.addAll(decks.filterNotNull())
            }
        } catch (e: Exception) {

        }

        return@coroutineScope deckList
    }

    suspend fun removeDeckFromCurrentUser(deckId: String) {
        val user = userRepository.getUser()

        if (user != null) {
            val userId = user.id

            val querySnapshot = firestore.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val userDoc = querySnapshot.documents[0]
                val userRef = userDoc.reference
                val currentDecks =
                    userDoc.get("savedDeck") as? MutableList<String> ?: mutableListOf()

                if (currentDecks.contains(deckId)) {
                    currentDecks.remove(deckId)
                }
                userRef.update("savedDeck", currentDecks).await()
            }
        }
    }

    suspend fun decrementCardIntervalsAndRetrieve(deckId: String): List<Card> {
        val user = userRepository.getUser()
        val cardsToReview = mutableListOf<Card>()

        if (user != null) {
            val userId = user.id

            val querySnapshot = firestore.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val userDoc = querySnapshot.documents[0]
                val userRef = userDoc.reference
                val deck = this.getDeckById(deckId)

                if (deck != null) {
                    val playedCard = userDoc.get("playedCards") as? MutableMap<String, MutableMap<String, Any>>
                        ?: mutableMapOf()

                    Log.d("Deck", deck.toString());

                    deck.cards.forEach { card ->
                        Log.d("Card", card.toString())

                        if (playedCard[card.id] != null){
                            playedCard[card.id]?.let {
                                val interval = (it["interval"] as? Long)?.toInt() ?: (it["interval"] as? Int)
                                Log.d("interval", interval.toString());
                                if (interval != null) {
                                    if (interval == 1) {
                                        cardsToReview.add(card)
                                    } else if (interval > 1) {
                                        it["interval"] = interval - 1
                                    }
                                }
                            }
                        }
                        else{
                            cardsToReview.add(card)
                        }

                    }
                    Log.d("a",cardsToReview.toString())
                    userRef.update("playedCards", playedCard).await()
                }
            }
        }

        return cardsToReview
    }



}
