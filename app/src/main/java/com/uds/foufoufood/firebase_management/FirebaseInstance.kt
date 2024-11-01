package com.uds.foufoufood.firebase_management

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

object FirebaseInstance {

    private val storage = Firebase.storage

    var storageRef = storage.reference

    suspend fun downloadAndCompressImageFromUrl(imageUrl: String, context: Context): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                // Use Glide to download the image
                val bitmap: Bitmap =
                    Glide.with(context).asBitmap().load(imageUrl).submit(500, 500).get()

                // Compress the image
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.WEBP, 60, outputStream)

                // Return the compressed image as a byte array
                outputStream.toByteArray()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}