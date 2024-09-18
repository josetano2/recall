import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import edu.bluejack23_2.recall.repository.DeckRepository
import edu.bluejack23_2.recall.repository.ReviewRepository
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.util.LocalNav
import kotlinx.coroutines.launch

class DeckDetailViewModel : ViewModel() {
    private val deckRepository: DeckRepository = DeckRepository()
    private val userRepository : UserRepository = UserRepository()
    private val reviewRepository : ReviewRepository = ReviewRepository()
    var deckName = mutableStateOf("")

    fun fetchDeckName(deckId: String) {
        viewModelScope.launch {
            val deck = deckRepository.getDeckById(deckId)
            deckName.value = deck?.name ?: ""
        }
    }

    suspend fun canReview(deckId : String) : Boolean{
        val user = userRepository.getUser();
        val userId = user?.id;

        return if (userId != null){
            reviewRepository.canReviewToday(userId, deckId);
        } else false
    }

    suspend fun startReview(deckId : String, mode : String) {
        val user = userRepository.getUser();
        val userId = user?.id;

        if (mode == "Rapid") return;

        if (userId != null){
            reviewRepository.recordReview(deckId = deckId, userId = userId)
        }
    }
}
