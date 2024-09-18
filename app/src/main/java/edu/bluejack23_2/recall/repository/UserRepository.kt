package edu.bluejack23_2.recall.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack23_2.recall.model.User
import edu.bluejack23_2.recall.ui.util.Collections
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createUser(username: String, email: String, password: String): String {
        var resp = ""
        try {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        resp = "Success"
                        // add username, pfp, empty array
                        val user = task.result?.user
                        user?.let {
                            val data = User(
                                id = user!!.uid,
                                username = username,
                                email = email,
                                playtime = 0f,
                                lastOpened = Timestamp.now(),
                                pfp = "",
                                savedDeck = listOf(),
                                playedCards = emptyMap()
                            )
                            FirebaseFirestore.getInstance()
                                .collection(Collections.USERS)
                                .document(user!!.uid).set(data)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        resp = "Success"
                                    }
                                    else{
                                        resp = "Error"
                                    }
                                }
                        }

                    } else {
                        resp = "Fail"
                    }
                }.await()
        } catch (e: FirebaseAuthUserCollisionException){
            resp = "Email already exist!"
        }
        catch (e: Exception) {
            resp = "Error"
        }

        return resp

    }

    suspend fun login(email: String, password: String): String {
        var resp = ""
        try {
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        resp = "Success"
                    } else {
                        resp = "Error"
                    }
                }.await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            resp = "Email is not formatted correctly!"
        } catch (e: Exception) {
            resp = "Invalid credentials!"
        }

        return resp
    }

    suspend fun getUser(): User? {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val querySnapshot = firestore.collection("users").document(user.uid).get().await()
            val data = querySnapshot.data

            val userData = data?.let { User.fromMap(it) }
            if (userData != null) return userData
        }
        return null
    }

    suspend fun getUserById(id: String): User? {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("id", id)
            .get()
            .await()

        return if (querySnapshot.isEmpty) {
            null
        } else {
            val user = querySnapshot.documents[0].data?.let { User.fromMap(it) }
            user
        }
    }

    fun updateProfilePicture(user: User, newPfp: String) {
        val docRef = firestore.collection(Collections.USERS).document(user.id)

        docRef.update("pfp", newPfp).addOnSuccessListener {
            Log.d("Success", "Success")
        }

    }

    suspend fun updateUsername(user: User, newUsername: String) : String{
        val docRef = firestore.collection(Collections.USERS).document(user.id)
        var resp = "Error"
        docRef.update("username", newUsername).addOnSuccessListener {
            resp = "Success"
        }.await()

        return resp
    }

    suspend fun getUsers(): List<User> {
        val querySnapshot = firestore.collection("users").get().await()
        val users = mutableListOf<User>()

        try {
            for (doc in querySnapshot.documents) {
                val map = doc.data
                if (map != null) {
                    val user = User.fromMap(map)
                    users.add(user)
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting user: ${e.message}", e)
        }


        return users
    }

    suspend fun logout(){
        FirebaseAuth.getInstance().signOut()
    }


}