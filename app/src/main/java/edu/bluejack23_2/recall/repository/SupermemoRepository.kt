package edu.bluejack23_2.recall.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.recall.model.supermemo.SupermemoMetadata
import kotlinx.coroutines.tasks.await

class SupermemoRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val userRepository: UserRepository = UserRepository()

    suspend fun getSupermemoByAggregateId(userId: String, cardId: String): SupermemoMetadata? {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            val documentSnapshot = querySnapshot.documents[0]
            val snapshot = documentSnapshot.data

            if (snapshot != null) {
                val playedCards =
                    snapshot["playedCards"] as? MutableMap<String, MutableMap<String, Any>>
                        ?: mutableMapOf()

                val cardMetadataMap = playedCards[cardId]
                Log.d("WOW",cardMetadataMap.toString())
                return cardMetadataMap?.let {
                    SupermemoMetadata(
                        easinessFactor = it["easinessFactor"] as? Double ?: 2.5,
                        repetition =  (it["repetition"] as? Long)?.toInt() ?: 0,
                        interval = (it["interval"] as? Long)?.toInt() ?: 0
                    )
                }
            }
        }
        return null
    }

//    suspend fun getSupermemoByAggregateId(userId: String, cardId: String): SupermemoMetadata?{
//        val querySnapshot = firestore.collection("users")
//            .whereEqualTo("id", userId)
//            .get()
//            .await()
//        if (!querySnapshot.isEmpty) {
//            val documentSnapshot = querySnapshot.documents[0]
//            val snapshot = documentSnapshot.data
//
//            if (snapshot != null) {
//                val playedCards =
//                    snapshot["playedCards"] as? MutableMap<String, MutableMap<String, Any>>
//                        ?: mutableMapOf()
//
//                return playedCards[cardId] as SupermemoMetadata;
//            }
//        }
//        return null;
//    }

    suspend fun addOrUpdateSupermemo(userId: String, cardId: String, metadata: SupermemoMetadata) {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            val documentSnapshot = querySnapshot.documents[0]
            val snapshot = documentSnapshot.data

            if (snapshot != null) {
                val playedCards =
                    snapshot["playedCards"] as? MutableMap<String, MutableMap<String, Any>>
                        ?: mutableMapOf()

                playedCards[cardId] = metadata.toMap().toMutableMap()

                firestore.collection("users").document(documentSnapshot.id)
                    .update("playedCards", playedCards)
                    .await()
            }
        }
    }
}
