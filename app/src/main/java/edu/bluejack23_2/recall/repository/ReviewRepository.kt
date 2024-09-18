package edu.bluejack23_2.recall.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class ReviewRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val userRepository: UserRepository = UserRepository()

    suspend fun canReviewToday(userId: String, deckId: String): Boolean {
        val currentDate = LocalDate.now().toString()
        val querySnapshot = firestore.collection("reviews")
            .whereEqualTo("userId", userId)
            .whereEqualTo("deckId", deckId)
            .whereEqualTo("reviewDate", currentDate)
            .get()
            .await()

        return querySnapshot.isEmpty
    }

    suspend fun recordReview(userId: String, deckId: String) {
        val reviewData = mapOf(
            "userId" to userId,
            "deckId" to deckId,
            "reviewDate" to LocalDate.now().toString()
        )

        firestore.collection("reviews").add(reviewData).await()
    }

}