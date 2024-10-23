package com.uds.foufoufood.Firebase_management

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

object FirebaseInstance {

    val storage = Firebase.storage

    var storageRef = storage.reference


//    fun deleteImage()
//    {
//
//    }
//
//    fun addNewImage():String
//    {
//
//    }
//
//    fun modifyImage():String
//    {
//
//    }

    fun compressImageToWebP(imageUri: Uri, context: Context): ByteArray {
        // Décoder l'image à partir de l'URI
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

        // Créer un ByteArrayOutputStream pour compresser l'image en WebP
        val outputStream = ByteArrayOutputStream()

        // Compresser l'image en WebP avec un taux de qualité (exemple : 80%)
        bitmap.compress(Bitmap.CompressFormat.WEBP, 60, outputStream)

        return outputStream.toByteArray() // Retourne l'image compressée en tableau de bytes
    }
}