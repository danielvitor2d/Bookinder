package com.mobile.bookinder.screens.my_books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.databinding.ActivityBookBinding

class BookActivity : AppCompatActivity() {

  private lateinit var binding: ActivityBookBinding
  private val bookDAO = BookDAO()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityBookBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val bookId = intent.getStringExtra("book_id")
    val book = bookDAO.findId(bookId)
    if(book != null){
      setUpListeners(book)
    }else{
      Toast.makeText(this, "Erro ao gerar página", Toast.LENGTH_LONG).show()
    }

  }

  private fun setUpListeners(book: Book) {
    binding.title.text = book.title
    binding.author.text = book.author
    binding.synopsis.text = book.synopsis
  }

  override fun onPause() {
    super.onPause()
  }

  override fun onStop() {
    super.onStop()
  }

}