package com.mobile.bookinder.screens.my_books

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.PhotoDAO
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
    binding.author.text = "Autor(a) " + book.author
    binding.gender.text = "Gênero: " + book.gender
    binding.synopsis.text = book.synopsis

    val photoDAO = PhotoDAO()
    for(photo_id in book.photos) {
      val photo = photoDAO.findById(photo_id)
      if (photo == null)
        continue

      val myBitmap = BitmapFactory.decodeFile(photo.path)
      binding.bookCover.setImageBitmap(myBitmap)
    }
  }
}