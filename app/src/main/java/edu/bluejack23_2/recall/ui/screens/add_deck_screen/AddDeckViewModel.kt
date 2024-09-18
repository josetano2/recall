package edu.bluejack23_2.recall.ui.screens.add_deck_screen


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack23_2.recall.model.Card
import edu.bluejack23_2.recall.model.Deck
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.repository.DeckRepository
import edu.bluejack23_2.recall.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class AddDeckViewModel : ViewModel() {
    private val deckRepository = DeckRepository()
    val deckName = mutableStateOf("")
    val showQuestionDialog = mutableStateOf(false)
    val questionText = mutableStateOf("")
    val answerText = mutableStateOf("")
    val currentCardIndex = mutableStateOf(0)
    val cards = mutableStateListOf<Card>()

    init {
        cards.add(Card(UUID.randomUUID().toString(),"", "", emptyList()))
    }

    suspend fun addDeck() : String {
        val userRepository = UserRepository();
        val user = userRepository.getUser();

        if(deckName.value.isNullOrEmpty()){
            return "Deck name must be filled!"
        }

        if(deckName.value.length > 50){
            return "Deck name cannot exceed 50 characters!"
        }


        if (user != null){
            Log.d("USER NULL CHECK", user.id);
            val deck = Deck(
                id = "",
                creatorId = user.id,
                name = deckName.value,
                cards = cards,
            )

            deckRepository.addDeck(deck)
            return "Success"

        }
        else{
            return "User is not logged in!"
        }
    }

    fun nextCard() {
        if (currentCardIndex.value < cards.size - 1) {
            currentCardIndex.value++
        } else {
            cards.add(Card(UUID.randomUUID().toString(),"", "", emptyList()))
            currentCardIndex.value = cards.size - 1
        }
    }

    fun previousCard() {
        if (currentCardIndex.value > 0) {
            currentCardIndex.value--
        }
    }

    fun handleOkButtonClick():  String {

        if(questionText.value.isNullOrEmpty()){
            return "Question cannot be empty!"
        }

        if(answerText.value.isNullOrEmpty()){
            return "Answer cannot be empty!"
        }

        if (questionText.value.isNotBlank() && currentCardIndex.value < cards.size) {
            cards[currentCardIndex.value] = cards[currentCardIndex.value].copy(
                question = questionText.value,
                answer = answerText.value
            )
            questionText.value = ""
            answerText.value = ""
            showQuestionDialog.value = false
            return "Success"
        }

        return "Error"
    }

    fun handleCancelButtonClick() {
        showQuestionDialog.value = false
    }

    fun handleRemoveButtonClick() {
//        Log.d("asdad", currentCardIndex.value.toString())
        if(currentCardIndex.value > 0){
            cards.remove(cards[currentCardIndex.value])
            currentCardIndex.value--
        }
        else{

            cards[0] = Card(UUID.randomUUID().toString(),"", "", emptyList())
        }



    }

    fun addQuestion() {
        showQuestionDialog.value = true
    }
}
