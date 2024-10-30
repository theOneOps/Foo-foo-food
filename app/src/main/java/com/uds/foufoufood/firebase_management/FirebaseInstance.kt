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
                // Utiliser Glide pour télécharger l'image en tant que Bitmap
                val bitmap: Bitmap =
                    Glide.with(context).asBitmap().load(imageUrl).submit(500, 500).get()

                // Compresser le bitmap en WebP
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.WEBP, 60, outputStream)

                // Retourner l'image compressée en ByteArray
                outputStream.toByteArray()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}