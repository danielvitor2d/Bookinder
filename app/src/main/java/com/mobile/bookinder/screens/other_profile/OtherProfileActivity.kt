package com.mobile.bookinder.screens.other_profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.*
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivityOtherProfileBinding
import com.mobile.bookinder.screens.my_books.BookActivity
import java.io.File
import java.util.*

class OtherProfileActivity: AppCompatActivity(), FeedCardBookEvent {

  private lateinit var binding: ActivityOtherProfileBinding
  private lateinit var books: MutableList<Book>
  private lateinit var bookView: RecyclerView

  private val bookDAO = BookDAO()
  private val userDAO = UserDAO()
  private val likeDAO = LikeDAO()
  private val matchDAO = MatchDAO()
  private val photoDAO = PhotoDAO()
  private val loggedUser = LoggedUser()

  private var user: User? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityOtherProfileBinding.inflate(layoutInflater)

    /*
      var bundle = Bundle()
      bundle.putString("user_id", userDAO.find("teste", "teste")?.user_id.toString())
      val intent = Intent(context, OtherProfileActivity::class.java)
      intent.putExtras(bundle)
      startActivity(intent)
     */

    val userId = intent.getStringExtra("user_id")
    if (userId != null) {
      user = userDAO.getById(UUID.fromString(userId))
    } else {
      Toast.makeText(this, "Erro!", Toast.LENGTH_SHORT).show()
    }

    setContentView(binding.root)

    setRecyclerView()
    setUpTextViews()
    setUpActionBar()

    val photo = photoDAO.findById(user?.photo_id) ?: return

    val file = File(photo.path)
    if (file.exists()) {
      val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
      val photoView = binding.imageViewPhotoUser
      photoView.setImageBitmap(myBitmap)
    }
  }

  override fun onResume() {
    super.onResume()

    updateRecyclerView()
  }

  private fun openBookPage(book: Book){
    val bundle = Bundle()
    bundle.putString("book_id", book.book_id.toString())

    val intent = Intent(binding.root.context, BookActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  private fun setUpTextViews() {
    "E-mail: ${user?.email}".also { binding.textViewEmail.text = it }
    "Nome Completo: ${user?.firstname} ${user?.lastname}".also { binding.textViewFullName.text = it }
    "Livros de ${user?.firstname}".also { binding.textViewSubtitleBooks.text = it }
  }

  private fun setRecyclerView() {
    bookView = binding.bookView
    bookView.layoutManager = LinearLayoutManager(binding.root.context)
  }

  fun updateRecyclerView() {
    books = bookDAO.allByUser(user?.user_id)
    bookView.adapter = BookAdapter(books, this)
  }

  override fun showCardBook(book: Book, position: Int) {
    openBookPage(book)
  }

  override fun likeBook(book: Book, position: Int) {
    val like = Like(UUID.randomUUID(), loggedUser.getUser()?.user_id!!, book.owner!!, book.book_id, Date(), null)
    likeDAO.insert(like)
    matchDAO.likeMacth(like)
  }

  override fun deslikeBook(book: Book, position: Int, like: Like?) {
    likeDAO.deslike(like)
    matchDAO.deslikeMatch(like)
  }

  private fun setUpActionBar() {
    setSupportActionBar(binding.toolbarFeedback)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Perfil"
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      android.R.id.home -> {
        finish()
        return true
      }
    }
    return super.onContextItemSelected(item)
  }

}