package com.mobile.bookinder.screens.my_books

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivityBookBinding

class BookActivity : AppCompatActivity() {

  private lateinit var binding: ActivityBookBinding
  private val db = Firebase.firestore
  private val storage = Firebase.storage


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityBookBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val book = intent.getSerializableExtra("book") as Book
    setUpListeners(book)
  }


  private fun setUpListeners(book: Book?) {

    db.collection("users")
      .whereEqualTo("user_id", book?.owner.toString())
      .get()
      .addOnCompleteListener {
        if (it.isSuccessful) {
          val user = it.result.documents[0].toObject<User>()

          binding.nameUser.text = user?.firstname
          binding.emailUser.text = user?.email
          val photoUser = user?.photo
          val bookCover = book?.photos?.get(0)
          val storageRef = storage.reference

          if (photoUser != null) {
            val imageRef = photoUser?.let { storageRef.child(it) }

            imageRef?.downloadUrl?.addOnSuccessListener {
              Glide.with(this)
                .load(it.toString())
                .centerCrop()
                .into(binding.profilePhoto)
            }
          }

          if (bookCover != null) {
            val imageRef = bookCover?.let { storageRef.child(it) }

            imageRef?.downloadUrl?.addOnSuccessListener {
              Glide.with(this)
                .load(it.toString())
                .centerCrop()
                .into(binding.bookCover)
            }
          }
        }
      }


    binding.title.text = book?.title
    binding.author.text = "Autor(a): ${book?.author}"
    binding.gender.text = "Gênero: ${book?.gender}"
    binding.synopsis.text = book?.synopsis


  }
}