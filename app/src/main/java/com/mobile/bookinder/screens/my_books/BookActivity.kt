package com.mobile.bookinder.screens.my_books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.bookinder.R

class BookActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_book)
    val bookId = intent.getStringExtra("book")
  }
}