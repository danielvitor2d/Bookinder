package com.mobile.bookinder.common.dao

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.common.model.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.*

class UserDAO {

  private val auth = Firebase.auth
  private val storage = Firebase.storage
  private val db = Firebase.firestore

  val storageRef = storage.reference
  val imagesRef = storageRef.child("images")


  fun insert(fieldName: String, fieldEmail: String, fieldPassword: String, profilePhoto: Uri?, context: Context) {
    if(fieldPassword.length < 8){
      Toast.makeText(context, "A senha deve conter no mínimo 8 caracteres", Toast.LENGTH_SHORT).show()
    }

    if (profilePhoto != null) {
      val file = profilePhoto
      val imagePath = "${UUID.randomUUID()}_${File(file.path).name}"
      val imageRef = imagesRef.child(imagePath)
      val uploadTask = imageRef.putFile(file)
      uploadTask
        .addOnProgressListener { uploadTask ->
          val progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
          Log.d(ContentValues.TAG, "Upload is $progress% done")
        }
        .addOnSuccessListener {
          createUser(fieldEmail, fieldPassword, fieldName, "images/${imagePath}", context)
        }
        .addOnFailureListener { e ->
          Toast.makeText(context, "Erro ao cadastrar imagem (Storage): $e", Toast.LENGTH_SHORT).show()
        }
    } else {
      createUser(fieldEmail, fieldPassword, fieldName, null, context)
    }
  }

  private fun createUser(email: String, password: String, firstname: String, imageID: String?, context: Context){
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener {  task ->
        if (task.isSuccessful) {
          val user = com.mobile.bookinder.common.models.User(email, firstname)

          if (imageID != null) {
            user.photo = imageID
          }

          db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
              // documentReference
              Toast.makeText(context, "Usuário cadastrado com sucesso ${documentReference.id}!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
              Toast.makeText(context, "Erro ao cadastrar usuário: $e", Toast.LENGTH_SHORT).show()
            }
        } else {
          Toast.makeText(context, "Erro ao cadastrar novo usuário", Toast.LENGTH_SHORT).show()
        }
      }
  }

  fun findUser(field: String, value: String): com.mobile.bookinder.common.models.User? {
    var user: com.mobile.bookinder.common.models.User? = null

    db.collection("users")
      .whereEqualTo(field, value)
      .get()
      .addOnSuccessListener {
        val dados = it.documents[0].data
        if (dados != null) {
          user = com.mobile.bookinder.common.models.User(dados.get("email").toString(),
            dados.get("firstname").toString())
          if (dados.get("lastname") != null) user!!.lastname = dados.get("lastname").toString()
          if (dados.get("photo") != null) user!!.photo = dados.get("photo").toString()
          if (dados.get("books") != null) user!!.books = dados.get("books") as MutableList<String?>
        }
      }
    return user
  }

  fun find(user_id: UUID): Boolean{ //saber se o usuário existe
    for (user in userList){
      if (user.user_id == user_id){
        return true
      }
    }
    return false
  }

  fun insertPhoto(user: User, photo: Photo): Boolean{
    if (find(user.user_id)){
      user.photo_id = photo.photo_id
      return true
    }
    return false
  }

  fun insertBook(user: User?, book_id: UUID?): Boolean{
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
    for (user in userList){
      if (user.user_id == _user.user_id){
        user.firstname = _user.firstname
        user.lastname = _user.lastname
        user.email = _user.email
      }
    }
  }

  fun getById(user_id: UUID?): User? {
    for (user in userList) {
      if (user.user_id == user_id) {
        return user
      }
    }
    return null
  }

  fun emailExists(email: String): Boolean{
    for (user in userList){
      if(user.email == email)
        return true
    }
    return false
  }

  companion object {
    val userList: MutableList<User> = mutableListOf()
  }
}
