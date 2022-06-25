package com.mobile.bookinder.screens.my_books

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.databinding.ActivityRegisterBookBinding
import com.mobile.bookinder.util.URIPathHelper
import java.net.URI
import java.util.*

class RegisterBook : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBookBinding
  private val loggedUser = LoggedUser()
  private var currentImages: MutableList<Uri> = mutableListOf()
  private var bookCoverUri: Uri? = null
  private var backCoverUri: Uri? = null
  val genders = arrayOf("Romântico", "Ficção científica", "Fantasia", "Conto", "Terror", "Aventura")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBookBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setUpListeners()
  }

  private fun setUpListeners() {
    //pegando imagens
    var text = ""
    val imageBookCover = registerForActivityResult(ActivityResultContracts.GetContent()){
     bookCoverUri = it
      text += "- ${it}\n"
      binding.tvImageList.text = text
    }

    val imageBackCover = registerForActivityResult(ActivityResultContracts.GetContent()){
      backCoverUri = it
      text += "- ${it}\n"
      binding.tvImageList.text = text
    }

    val imageBookPages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
      currentImages = it

      for(uri in currentImages){
        text += "- ${uri}\n"
      }

      binding.tvImageList.text = text
    }

    //pegando o genero
    val spinner = binding.spinner
    var fieldGender = ""
    val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genders)
    spinner.adapter = arrayAdapter
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        fieldGender = genders[p2]
      }

      override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
      }

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
      val user = loggedUser.getUser()
      val check = fieldChecklist(fieldTitle, fieldAuthor, bookCoverUri)

      if (user != null && check) {
        val book = Book(UUID.randomUUID(), fieldTitle, fieldAuthor, fieldGender, fieldSynopsis, user.user_id)
        val bookDAO = BookDAO()
        bookDAO.insert(book, user)

        val photoDAO = PhotoDAO()
        val uriPath = URIPathHelper()
        photoDAO.insert(Photo(UUID.randomUUID(), uriPath.getPath(this, bookCoverUri as Uri).toString()), book.book_id)
        if(backCoverUri is Uri){
          photoDAO.insert(Photo(UUID.randomUUID(), uriPath.getPath(this, backCoverUri as Uri).toString()), book.book_id)
        }
        for(uri in currentImages){
          photoDAO.insert(Photo(UUID.randomUUID(), uriPath.getPath(this, uri).toString()), book.book_id)
        }

        Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()
        finish()
      }else{
        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun fieldChecklist(title: String, author: String, bookCover: Uri?): Boolean{
    if(title.equals("") || author.equals("") || bookCover == null){
      return false
    }
    return true
  }

  override fun onPause() {
    super.onPause()
  }

  override fun onStop() {
    super.onStop()
  }
}