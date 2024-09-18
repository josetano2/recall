package edu.bluejack23_2.recall.ui.screens.homepage_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.recall.model.Deck
import edu.bluejack23_2.recall.model.interface_.DeckViewModel
import edu.bluejack23_2.recall.repository.DeckRepository
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.util.search.Search
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomepageViewModel : ViewModel(), DeckViewModel {
    private val deckRepository: DeckRepository = DeckRepository()
    private val userRepository : UserRepository = UserRepository()
    private val search : Search = Search()

    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> get() = _decks
    private var fixedDecks : List<Deck> = emptyList();

    var currentUserDecks = MutableStateFlow<List<String>>(emptyList())
    var loading = MutableStateFlow(true)

    var currentUserId = MutableStateFlow<String>("");
    init {
        getDecks()
    }

    override fun getDecks() {
        viewModelScope.launch {
            loading.value = true
            val fetchedDecks = deckRepository.getUserDeck()
            _decks.value = fetchedDecks
            fixedDecks = fetchedDecks.toMutableList();

            val user = userRepository.getUser();
            if (user != null){
                currentUserDecks.value = user.savedDeck;
                currentUserId.value = user.id;
            }
            loading.value = false
        }
    }

    override fun performLinearSearch(query: String, type : String?) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _decks.value = deckRepository.getDecks()
            } else {
                val searchResults = search.linearSearchDeck(decks.value, query)
                _decks.value = searchResults
            }
        }
    }
    override fun performFuzzySearch(query: String, type : String?){
        viewModelScope.launch {
            if (query.isBlank()) {
                _decks.value = deckRepository.getDecks()
            } else {
                val searchResults = search.fuzzySearchDeck(fixedDecks, query)
                _decks.value = searchResults
            }
        }
    }

    override suspend fun getUsername(id : String) : String {
        val user = userRepository.getUserById(id);
        return user?.username ?: ""
    }

    override fun addDeckToCurrentUser(deckId: String) {
        viewModelScope.launch {
            async {
                deckRepository.addDeckToCurrentUser(deckId)
            }.await()
            val user = userRepository.getUser()
            user?.let {
                currentUserDecks.value = it.savedDeck
            }
            getDecks()
        }
    }


    override fun removeDeckFromCurrentUser(deckId: String) {
        viewModelScope.launch {
            async {
                deckRepository.removeDeckFromCurrentUser(deckId)
            }.await()
            val user = userRepository.getUser()
            user?.let {
                currentUserDecks.value = it.savedDeck
            }
            getDecks()
        }
    }



}
