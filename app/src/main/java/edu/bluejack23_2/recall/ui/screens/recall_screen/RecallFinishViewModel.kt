package edu.bluejack23_2.recall.ui.screens.recall_screen

import ScoredCard
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.recall.model.supermemo.SupermemoMetadata
import edu.bluejack23_2.recall.repository.ReviewRepository
import edu.bluejack23_2.recall.repository.SupermemoRepository
import edu.bluejack23_2.recall.repository.UserRepository
import edu.bluejack23_2.recall.ui.util.algorithm.Supermemo
import java.util.*

class RecallFinishViewModel : ViewModel() {
    private val _storedCards = MutableLiveData<List<ScoredCard>>()
    private val userRepository = UserRepository()
    private val supermemoRepository = SupermemoRepository()
    val storedCards: LiveData<List<ScoredCard>> = _storedCards
    private val mode = MutableLiveData<String>()

    fun setStoredCards(cards: List<ScoredCard>) {
        _storedCards.value = cards
    }

    fun setMode(mode : String){
        this.mode.value = mode;
    }

    suspend fun supermemoSave() {
        val user = userRepository.getUser()
        val userId = user?.id

        mode.value?.let { Log.d("woi", it) };
        if (mode.value == "Rapid") return;

        if (userId != null) {
            _storedCards.value?.let { cards ->
                for (card in cards) {
                    val grade = getGrade(card.response)
                    val supermemoMetadata: SupermemoMetadata? = supermemoRepository.getSupermemoByAggregateId(userId, card.card.id)
                    Log.d("Supermemo Metadata", supermemoMetadata.toString())
                    if (supermemoMetadata != null) {
                        val repetition = supermemoMetadata.repetition
                        val easinessFactor = supermemoMetadata.easinessFactor
                        val interval = supermemoMetadata.interval


                        val result = Supermemo().supermemo2(grade, repetition, easinessFactor, interval)
                        val newMetadata = SupermemoMetadata(result.easinessFactor, result.repetition, result.interval)

                        supermemoRepository.addOrUpdateSupermemo(userId, card.card.id, newMetadata)
                    } else {
                        supermemoRepository.addOrUpdateSupermemo(userId, card.card.id, SupermemoMetadata(2.5, 1,1))
                    }

                }
            }
        }
    }

    private fun getGrade(grade: String): Supermemo.Grade {
        return when (grade.lowercase(Locale.ROOT)) {
            "again" -> Supermemo.Grade.Again
            "hard" -> Supermemo.Grade.Hard
            "good" -> Supermemo.Grade.Good
            "easy" -> Supermemo.Grade.Easy
            else -> throw IllegalArgumentException("Unknown grade: $grade")
        }
    }
}
