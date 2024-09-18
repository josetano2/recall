package edu.bluejack23_2.recall.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ImageRepository {

    fun uploadImage(uri: Uri, context: Context, callback: (Uri?) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val uniqueImageName = UUID.randomUUID().toString()
        val spaceRef: StorageReference = storageRef.child("$uniqueImageName.jpg")

        val byteArray: ByteArray? =
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

        byteArray?.let {
            var uploadTask = spaceRef.putBytes(byteArray)
            uploadTask.addOnCompleteListener {
//                task -> Log.d("Asdada", "${task.result}")
                spaceRef.downloadUrl.addOnSuccessListener {
                   downloadUri -> callback(downloadUri)
                }.addOnFailureListener{
                    callback(null)
                }
            }
        }


    }
}