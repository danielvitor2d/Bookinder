package com.mobile.bookinder.screens.my_books

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.databinding.ActivityRegisterBookBinding
import java.io.File
import java.util.*


class RegisterBook : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBookBinding
  private val loggedUser = LoggedUser()
  private var currentImages: MutableList<Uri> = ArrayList(0)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBookBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setUpListeners()
  }

  private fun setUpListeners() {


    val getImage = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
      currentImages = it

      var text = ""
      for(uri in currentImages){
        text += "- ${uri}\n"
      }

      binding.tvImageList.text = text
    }

    binding.addImage.setOnClickListener {
      getImage.launch("image/*")
    }
    binding.register.setOnClickListener {
      val fieldTitle = binding.editTextTitle.text.toString()
      val fieldAuthor = binding.editTextAuthor.text.toString()
      val fieldSynopsis = binding.editTextSynopsis.text.toString()
      val user = loggedUser.getUser()
      val check = fieldChecklist(fieldTitle, fieldAuthor, fieldSynopsis)

      if (user != null && check) {
        val book = Book(UUID.randomUUID(), fieldTitle, fieldAuthor, fieldSynopsis, user.user_id)
        val bookDAO = BookDAO()
        bookDAO.insert(book, user)
        Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()
        finish()
      }else{
        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
      }
    }
  }

  private fun fieldChecklist(title: String, author: String, synopsis: String): Boolean{
    if(title.equals("") || author.equals("")){
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