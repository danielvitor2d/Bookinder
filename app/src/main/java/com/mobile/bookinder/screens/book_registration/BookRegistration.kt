package com.mobile.bookinder.screens.book_registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.BookRegistrationBinding
import com.mobile.bookinder.databinding.HomeBinding
import com.mobile.bookinder.databinding.ToolbarBinding
import com.mobile.bookinder.screens.my_books.MyBooksFragment

class BookRegistration : AppCompatActivity() {
  private lateinit var binding: BookRegistrationBinding
  private lateinit var bindingToolbar: ToolbarBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = BookRegistrationBinding.inflate(layoutInflater)
    setContentView(binding.root)
     //setContentView(R.layout.book_registration)

    bindingToolbar = ToolbarBinding.inflate(layoutInflater)
    val toolbar = bindingToolbar.toolbar;
    setSupportActionBar(toolbar)
  }
}