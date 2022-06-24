package com.mobile.bookinder.screens.my_books

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.databinding.ActivityRegisterBookBinding
import java.util.*

class RegisterBook : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBookBinding
  private val loggedUser = LoggedUser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBookBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setUpListeners();
  }

  private fun setUpListeners() {
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
      } else{
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
}