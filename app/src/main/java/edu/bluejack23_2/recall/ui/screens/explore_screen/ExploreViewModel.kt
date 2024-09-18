package edu.bluejack23_2.recall.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import edu.bluejack23_2.recall.model.Deck
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.model.interface_.DeckViewModel
import edu.bluejack23_2.recall.repository.DeckRepository
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.util.search.Search
import kotlinx.coroutines.async

class ExploreViewModel : ViewModel(), DeckViewModel {
    private val deckRepository : DeckRepository = DeckRepository()
    private val userRepository : UserRepository = UserRepository()
    private val search : Search = Search()

    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> get() = _decks
    private var fixedDecks : List<Deck> = emptyList();

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users : StateFlow<List<User>> get() = _users
    private var fixedUsers : List<User> = emptyList();

    var currentUserDecks = MutableStateFlow<List<String>>(emptyList())
    var loading = MutableStateFlow(true)

    var currentUserId = MutableStateFlow<String>("");
    init {
        getDecks()
    }

    override fun getDecks() {

        viewModelScope.launch {
            loading.value = true
            val fetchedDecks = deckRepository.getDecks()
            _decks.value = fetchedDecks
            fixedDecks = fetchedDecks.toMutableList();

            val fetchedUsers = userRepository.getUsers();
            _users.value = fetchedUsers
            fixedUsers = fetchedUsers.toMutableList()

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
            if (type != null && type == "Deck"){
                if (query.isBlank()) {
                    _decks.value = deckRepository.getDecks()
                } else {
                    val searchResults = search.linearSearchDeck(decks.value, query)
                    _decks.value = searchResults
                }
            }
            else if (type != null && type == "User"){
                if (query.isBlank()) {
                    _users.value = userRepository.getUsers()
                } else {
                    val searchResults = search.linearSearchUser(users.value, query)
                    _users.value = searchResults
                }
            }
        }
    }
    override fun performFuzzySearch(query: String,  type : String?){
        viewModelScope.launch {
            if (type != null && type == "Deck"){
                if (query.isBlank()) {
                    _decks.value = deckRepository.getDecks()
                } else {
                    val searchResults = search.fuzzySearchDeck(fixedDecks, query)
                    _decks.value = searchResults
                }
            }
            else if (type != null && type == "User"){
                if (query.isBlank()) {
                    _users.value = userRepository.getUsers()
                } else {
                    val searchResults = search.fuzzySearchUser(fixedUsers, query)
                    _users.value = searchResults
                }
            }
        }
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
            Log.d("HELLOOOOOOOOO", currentUserDecks.value.toString())
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
            Log.d("HELLOOOOOOOOO", currentUserDecks.value.toString())

        }
    }

    override suspend fun getUsername(id : String) : String {
        val user = userRepository.getUserById(id);
        return user?.username ?: ""
    }
}
