package com.mobile.bookinder.screens.my_books

import android.R
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.databinding.ActivityMyBookBinding
import com.mobile.bookinder.util.URIPathHelper
import java.util.*


class MyBookActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMyBookBinding
  private lateinit var bookTitle: EditText
  private lateinit var bookAuthor: EditText
  private lateinit var bookGender: Spinner
  private lateinit var bookSynopsis: EditText
  private var currentImages: MutableList<Uri> = mutableListOf()

  private val bookDAO = BookDAO()
  val genders = arrayOf("Romântico", "Ficção científica", "Fantasia", "Conto", "Terror", "Aventura")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMyBookBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val bookId = intent.getStringExtra("book_id")
    val book = bookDAO.findId(bookId)
    setUpViews()
    if(book != null){
      setUpListeners(book)
    }else{
      Toast.makeText(this, "Erro ao gerar página", Toast.LENGTH_LONG).show()
    }
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
    //pegando imagens
    var text = ""
    val imageBookPages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
      currentImages = it

      for(uri in currentImages){
        text += "- ${uri}\n"
      }

      binding.tvImageList.text = text
    }

    //pegando o genero
    var gender = ""
    val arrayAdapter = ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, genders)
    bookGender.adapter = arrayAdapter
    bookGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        gender = genders[p2]
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
      }

    }

    var pagesRemoved = false

    //clicando nos botões de imagens
    binding.buttonBookPages.setOnClickListener {
      imageBookPages.launch("image/*")
      pagesRemoved = true
    }

    //fazer alterações
    binding.alter.setOnClickListener {
      val title = bookTitle.text.toString()
      val author = bookAuthor.text.toString()
      val synopsis = bookSynopsis.text.toString()

      val photos = book.photos
      val user = book.owner
      val bookDAO = BookDAO()

      val check = fieldChecklist(title, author, currentImages, pagesRemoved)
      if (check) {
        bookDAO.alterBook(book, title, author, gender, synopsis, photos)
        if(pagesRemoved){
          bookDAO.deletePhotos(book)
          val photoDAO = PhotoDAO()
          val uriPath = URIPathHelper()
          for(uri in currentImages){
            photoDAO.insert(Photo(UUID.randomUUID(), uriPath.getPath(this, uri).toString()), book.book_id)
          }
        }

        Toast.makeText(this, "Alterado com sucesso", Toast.LENGTH_LONG).show()
        finish()
      }else{
        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
      }
    }

  }

  private fun fieldChecklist(title: String, author: String, images: MutableList<Uri>, removed: Boolean): Boolean{
    if(title.equals("") || author.equals("")){
      return false
    }else if(images.size == 0 && removed){
      return false
    }
    return true
  }
}