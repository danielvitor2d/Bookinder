package com.mobile.bookinder.common.dao

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.common.model.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class UserDAO {

  private val auth = Firebase.auth
  private val storage = Firebase.storage
  private val db = Firebase.firestore

  private val storageRef = storage.reference
  private val imagesRef = storageRef.child("images")

//  fun insert(
//    firstname: String,
//    email: String,
//    password: String,
//    profilePhoto: Uri?,
//    context: Context,
//  ) {
//    if (password.length < 8) {
//      Toast.makeText(context, "A senha deve conter no mínimo 8 caracteres", Toast.LENGTH_SHORT)
//        .show()
//    }
//
//    if (profilePhoto != null && profilePhoto.path != null && profilePhoto.path?.isNotEmpty() == true) {
//      val imagePath = "${UUID.randomUUID()}_${File(profilePhoto.path).name}"
//      val imageRef = imagesRef.child(imagePath)
//      val uploadTask = imageRef.putFile(profilePhoto)
//
//      uploadTask
//        .addOnProgressListener { uploadTask ->
//          val progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
//          Log.d(ContentValues.TAG, "Upload is $progress% done")
//        }
//        .addOnSuccessListener {
//          createUser(email, password, firstname, "images/${imagePath}", context)
//        }
//        .addOnFailureListener { e ->
//          Toast.makeText(context, "Erro ao cadastrar imagem (Storage): $e", Toast.LENGTH_SHORT)
//            .show()
//        }
//    } else {
//      createUser(email, password, firstname, null, context)
//    }
//  }

//  private fun createUser(
//    email: String,
//    password: String,
//    firstname: String,
//    imageID: String?,
//    context: Context,
//  ) {
//    auth.createUserWithEmailAndPassword(email, password)
//      .addOnCompleteListener { task ->
//        if (task.isSuccessful) {
//          val user = User(firstname, email)
//
//          if (imageID != null) {
//            user.photo = imageID
//          }
//
//          db.collection("users").document(user.user_id.toString())
//            .set(user)
//            .addOnSuccessListener {
//              Toast.makeText(context, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//              Toast.makeText(context, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
//              e.message?.let { Log.d("Erro cadastrar usuario", it) }
//            }
//        } else {
//          Toast.makeText(context, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
//        }
//      }
//  }

  suspend fun find(user_id: String): Boolean {
    return try {
      val data = db.collection("users")
        .document(user_id)
        .get()
        .await()
      data.exists()
    } catch (e: FirebaseFirestoreException) {
      false
    }
  }

  suspend fun insertPhoto(user: User, photoUri: Uri?): Boolean {
    if (user.user_id == null || photoUri == null) return false
    if (find(user.user_id.toString()) && photoUri.path != null && photoUri.path!!.isNotEmpty()) {
      val imageRef = imagesRef.child("${UUID.randomUUID()}_${File(photoUri.path).name}")
      val uploadTask = imageRef.putFile(photoUri)
      return try {
        uploadTask.await()
        true
      } catch (e: Exception) {
        false
      }
    }
    return false
  }

  fun insertBook(user: User?, book_id: String?): Boolean {
    val bookDAO = BookDAO()
    if (bookDAO.findId(book_id) != null) {
      return false
    }
    if (book_id != null) {
      user?.books?.add(book_id)
    }
    return true
  }

  fun setUser(_user: User) {
    for (user in userList) {
      if (user.user_id == _user.user_id) {
        user.firstname = _user.firstname
        user.lastname = _user.lastname
        user.email = _user.email
      }
    }
  }

  fun getById(user_id: String?): User? {
    for (user in userList) {
      if (user.user_id == user_id) {
        return user
      }
    }
    return null
  }

  fun emailExists(email: String): Boolean {
    for (user in userList) {
      if (user.email == email)
        return true
    }
    return false
  }

  companion object {
    val userList: MutableList<User> = mutableListOf()
  }
}
