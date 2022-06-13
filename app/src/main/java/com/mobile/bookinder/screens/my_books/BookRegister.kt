package com.mobile.bookinder.screens.my_books

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.mobile.bookinder.common.Book
import com.mobile.bookinder.databinding.ActivityBookRegisterBinding
import com.mobile.bookinder.screens.dao.BookDAO
import com.mobile.bookinder.screens.home.Home


class BookRegister : AppCompatActivity() {
  private lateinit var binding: ActivityBookRegisterBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityBookRegisterBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setUpListeners();

  }

  private fun setUpListeners() {
    binding.bookregister.setOnClickListener {
      val fieldTitle = binding.editTextTitle.text.toString()
      val fieldAuthor = binding.editTextAuthor.text.toString()
      val fieldSynopsis = binding.editTextSynopsis.text.toString()
      //pegar o id do usuário logado
      val user_id = intent.getIntExtra("user_id", -1) ?: -1

      val bookDAO = BookDAO()
      val book = Book(bookDAO.newId(), fieldTitle, fieldAuthor, fieldSynopsis, user_id)
      if (bookDAO.insert(book)){
        Snackbar.make(this, it,"Livro cadastrado!", Snackbar.LENGTH_LONG).show()
      }else{
        Snackbar.make(this, it,"Erro ao cadastrar.", Snackbar.LENGTH_LONG).show()
      }
    }
  }
}