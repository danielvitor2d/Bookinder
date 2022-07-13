package com.mobile.bookinder.screens.my_books

import android.R
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.databinding.ActivityMyBookBinding
import java.io.File
import java.util.*

class MyBookActivity : AppCompatActivity() {
  private var db = Firebase.firestore
  private var storage = Firebase.storage

  private val storageRef = storage.reference
  private val imagesRef = storageRef.child("images")

  private lateinit var binding: ActivityMyBookBinding
  private lateinit var bookTitle: EditText
  private lateinit var bookAuthor: EditText
  private lateinit var bookGender: Spinner
  private lateinit var bookSynopsis: EditText
  private var currentImages: MutableList<Uri> = mutableListOf()

  val genders = arrayOf("Romântico", "Ficção científica", "Fantasia", "Conto", "Terror", "Aventura")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMyBookBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val book = intent.getSerializableExtra("book") as Book

    setUpViews()
    setUpListeners(book)
  }

  private fun setUpViews(){
    bookTitle = binding.editTextTitle
    bookAuthor = binding.editTextAuthor
    bookGender = binding.gender
    bookSynopsis = binding.editTextSynopsis
  }

  private fun setUpListeners(book: Book) {
    bookTitle.setText(book.title)
    bookAuthor.setText(book.author)
    bookSynopsis.setText(book.synopsis)

    val imageBookPages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
      currentImages = it
    }

    var gender = ""
    val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, genders)
    bookGender.adapter = arrayAdapter
    bookGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        gender = genders[p2]
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    binding.buttonBookPages.setOnClickListener {
      imageBookPages.launch("image/*")
    }

    binding.alter.setOnClickListener {
      val title = bookTitle.text.toString()
      val author = bookAuthor.text.toString()
      val synopsis = bookSynopsis.text.toString()

      val check = fieldChecklist(title, author, synopsis)
      if (check) {

        val updates = hashMapOf<String, Any>(
          "title" to title,
          "author" to author,
          "synopsis" to synopsis,
          "gender" to gender,
        )

        var imagePath = ""
        if (currentImages.size > 0 && currentImages[0].path != null && currentImages[0].path?.isNotEmpty() == true) {
          imagePath = "${UUID.randomUUID()}_${currentImages[0].path?.let { path -> File(path).name }}"
          updates["photos"] = mutableListOf("images/$imagePath")
          val imageRef = imagesRef.child(imagePath)
          imageRef.putFile(currentImages[0])
          val lastPhotoRef = book.photos!![0].let { lastPhoto -> storageRef.child(lastPhoto) }
          lastPhotoRef.delete()
        }

        db.collection("books").document(book.book_id.toString()).get()
          .addOnSuccessListener {
            it.reference.update(updates)
            Toast.makeText(applicationContext, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
          }
          .addOnFailureListener {
            Toast.makeText(applicationContext, "Erro ao atualizar livro", Toast.LENGTH_SHORT).show()
            finish()
          }

//
//      val photos = book.photos
//      val user = book.owner
//      val bookDAO = BookDAO()
//
//      val check = fieldChecklist(title, author, currentImages, pagesRemoved)
//      if (check) {
//        bookDAO.alterBook(book, title, author, gender, synopsis, photos)
//        if(pagesRemoved){
//          bookDAO.deletePhotos(book)
//          val photoDAO = PhotoDAO()
//          val uriPath = URIPathHelper()
//          for(uri in currentImages){
//            photoDAO.insert(Photo(UUID.randomUUID().toString(), uriPath.getPath(this, uri).toString()), book.book_id)
//          }
//        }
//
//        Toast.makeText(this, "Alterado com sucesso", Toast.LENGTH_LONG).show()
//        finish()
//      } else {
//        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
//      }
      }
    }
  }

  private fun fieldChecklist(title: String, author: String, synopsis: String): Boolean {
    return !(title.isEmpty() || author.isEmpty() || synopsis.isEmpty())
  }
}