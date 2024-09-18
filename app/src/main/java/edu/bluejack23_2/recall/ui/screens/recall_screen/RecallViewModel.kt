import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.recall.model.Deck
import edu.bluejack23_2.recall.model.Card
import edu.bluejack23_2.recall.repository.DeckRepository
import kotlinx.coroutines.launch

class RecallViewModel : ViewModel() {
    private val deckRepository = DeckRepository()
    var deck = mutableStateOf<Deck?>(null)
    var cards = mutableStateOf<List<Card>>(emptyList())
    var index = mutableStateOf(0)
    var storedCards = mutableStateOf<List<ScoredCard>>(emptyList())
    var initialLength = mutableStateOf(0)
    var numberOfCards = mutableStateOf(0)
    private var onLastCardReached: (() -> Unit)? = null

    fun setOnLastCardReachedCallback(callback: () -> Unit) {
        onLastCardReached = callback
    }


    fun fetchDeck(deckId: String, mode: String, numberOfCards: Int) {
        viewModelScope.launch {
            val retrievedCards = if (mode == "Review") {
                deckRepository.decrementCardIntervalsAndRetrieve(deckId)
            } else {
                deckRepository.getDeckById(deckId)?.cards
            }
            val shuffledCards = retrievedCards?.let { shuffleCards(it) }

            if (shuffledCards != null) {
                val limitedCards = shuffledCards.take(numberOfCards)

                try {
                    val deck_ = deckRepository.getDeckById(deckId)
                    deck_?.let {
                        deck.value = it
                        cards.value = limitedCards
                        initialLength.value = limitedCards.size
                    } ?: run {
                        Log.e("RecallViewModel", "Deck not found")
                    }
                } catch (e: Exception) {
                    Log.e("RecallViewModel", "Error fetching deck", e)
                }
            }
        }
    }




    fun shuffleCards(cards: List<Card>): List<Card> {
        return cards.shuffled()
    }

    fun nextCard(response: String) {
        if (cards.value.isNotEmpty()) {
            storeCard(response)

            if (response == "AGAIN") {
                moveCard()
            } else {
                if (cards.value.size == 1) {
                    handleLastCard()
                }
                removeFirstCard()
            }

            Log.d("RecallViewModel", "Index: ${index.value}, Size: ${cards.value.size}")
        }
    }

    private fun removeFirstCard() {
        updateCards { it.removeAt(0) }
    }

    private fun moveCard() {
        updateCards {
            val movedCard = it.removeAt(0)
            it.add(movedCard)
        }
    }

    private fun storeCard(response: String) {
        if (storedCards.value.size < initialLength.value) {
            val scoredCard = ScoredCard(cards.value.first(), response)
            storedCards.value = storedCards.value + scoredCard

            Log.d("RecallViewModel", "Stored Cards: ${storedCards.value}, Size: ${storedCards.value.size}")
        }
    }

    private fun handleLastCard() {
        Log.d("RecallViewModel", "LAST: ${storedCards.value}")
        onLastCardReached?.invoke()
    }

    private fun updateCards(operation: (MutableList<Card>) -> Unit) {
        val updatedCards = cards.value.toMutableList()
        operation(updatedCards)
        cards.value = updatedCards
    }


}

data class ScoredCard(
    val card: Card,
    val response: String
)
