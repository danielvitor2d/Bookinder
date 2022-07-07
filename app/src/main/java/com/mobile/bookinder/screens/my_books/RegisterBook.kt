package com.mobile.bookinder.screens.my_books

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.databinding.ActivityRegisterBookBinding
import com.mobile.bookinder.util.URIPathHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class RegisterBook : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBookBinding
  private var currentImages: MutableList<Uri> = mutableListOf()
  private var bookCoverUri: Uri? = null
  private var backCoverUri: Uri? = null
  val genders = arrayOf("Romântico", "Ficção científica", "Fantasia", "Conto", "Terror", "Aventura")

  private var db = Firebase.firestore
  private var auth = Firebase.auth
  private var storage = Firebase.storage

  private var storageRef = storage.reference
  private var imagesRef = storageRef.child("images")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBookBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setUpListeners()
  }

  private fun setUpListeners() {
    val imageBookCover = registerForActivityResult(ActivityResultContracts.GetContent()) {
      bookCoverUri = it
    }

    val imageBackCover = registerForActivityResult(ActivityResultContracts.GetContent()) {
      backCoverUri = it
    }

    val imageBookPages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
      currentImages = it
    }

    val spinner = binding.spinner
    var fieldGender = ""
    val arrayAdapter =
      ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
    spinner.adapter = arrayAdapter
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        fieldGender = genders[p2]
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    binding.buttonBookPages.setOnClickListener {
      imageBookPages.launch("image/*")
    }

    binding.buttonBookCover.setOnClickListener {
      imageBookCover.launch("image/*")
    }

    binding.buttonBackCover.setOnClickListener {
      imageBackCover.launch("image/*")
    }

    binding.register.setOnClickListener {
      val fieldTitle = binding.editTextTitle.text.toString()
      val fieldAuthor = binding.editTextAuthor.text.toString()
      val fieldSynopsis = binding.editTextSynopsis.text.toString()

      val check = fieldChecklist(fieldTitle, fieldAuthor, bookCoverUri)

      if (check) {

        val listOfPhotos = mutableListOf<String>()

        if (bookCoverUri != null && bookCoverUri?.path?.isNotEmpty() == true) {
          val imagePath = "${UUID.randomUUID()}_${bookCoverUri!!.path?.let { path -> File(path).name }}"
          listOfPhotos.add("images/$imagePath")
          val imageRef = imagesRef.child(imagePath)
          val uploadTask = imageRef.putFile(bookCoverUri!!)
          GlobalScope.launch {
            uploadTask.await()
          }
        }

        if (backCoverUri != null && backCoverUri?.path?.isNotEmpty() == true) {
          val imagePath = "${UUID.randomUUID()}_${backCoverUri!!.path?.let { path -> File(path).name }}"
          listOfPhotos.add("images/$imagePath")
          val imageRef = imagesRef.child(imagePath)
          val uploadTask = imageRef.putFile(backCoverUri!!)
          GlobalScope.launch {
            uploadTask.await()
          }
        }

        for (uri in currentImages) {
          if (uri.path?.isNotEmpty() == true) {
            val imagePath = "${UUID.randomUUID()}_${uri.path?.let { path -> File(path).name }}"
            listOfPhotos.add("images/$imagePath")
            val imageRef = imagesRef.child(imagePath)
            val uploadTask = imageRef.putFile(uri)
            GlobalScope.launch {
              uploadTask.await()
            }
          }
        }

        val book = Book(UUID.randomUUID().toString(),
          fieldTitle,
          fieldAuthor,
          fieldGender,
          fieldSynopsis,
          auth.currentUser?.uid,
          listOfPhotos)

        book.book_id?.let { id ->
          db.collection("books").document(id).set(book).addOnCompleteListener { task ->
            if (task.isSuccessful) {
              Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()
              finish()
            } else {
              Toast.makeText(this, "Erro ao cadastrar livro", Toast.LENGTH_LONG).show()
            }
          }
        }

      } else {
        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun fieldChecklist(title: String, author: String, bookCover: Uri?): Boolean {
    return !(title.isEmpty() || author.isEmpty() || bookCover == null)
  }
}