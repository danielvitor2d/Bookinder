package com.mobile.bookinder.screens.my_books

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivityBookBinding

class BookActivity : AppCompatActivity() {

  private lateinit var binding: ActivityBookBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityBookBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val bookId = intent.getStringExtra("book_id")

//    setUpListeners(book, user)
  }

  private fun setUpListeners(book: Book?, user: User?) {
    val photoDAO = PhotoDAO()

    binding.nameUser.text = user?.firstname
    binding.emailUser.text = user?.email
    binding.title.text = book?.title
    binding.author.text = book?.author
    binding.gender.text = book?.gender
    binding.synopsis.text = book?.synopsis

    val coverPhoto = photoDAO.findById(book?.photos?.get(0))
    val myBitmap = BitmapFactory.decodeFile(coverPhoto?.path)
    binding.bookCover.setImageBitmap(myBitmap)

    if (user?.photo != null){
//      val profilePhoto = photoDAO.findById(user?.photo)
//      val myBitmap2 = BitmapFactory.decodeFile(profilePhoto?.path)
//      binding.profilePhoto.setImageBitmap(myBitmap2)
    }
  }
}