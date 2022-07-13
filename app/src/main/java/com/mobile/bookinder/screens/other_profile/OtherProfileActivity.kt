package com.mobile.bookinder.screens.other_profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.LikeDAO
import com.mobile.bookinder.common.dao.MatchDAO
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivityOtherProfileBinding
import com.mobile.bookinder.screens.my_books.BookActivity
import java.util.*

class OtherProfileActivity: AppCompatActivity(), FeedCardBookEvent {

  private lateinit var binding: ActivityOtherProfileBinding
  private lateinit var books: MutableList<Book>
  private lateinit var bookView: RecyclerView

  private val bookDAO = BookDAO()
  private val userDAO = UserDAO()
  private val likeDAO = LikeDAO()
  private val matchDAO = MatchDAO()
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
      user = userDAO.getById(userId)
    } else {
      Toast.makeText(this, "Erro!", Toast.LENGTH_SHORT).show()
    }

    setContentView(binding.root)
    setRecyclerView()
    setUpTextViews()
    setUpActionBar()
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
    binding.textViewEmail.text = "E-mail: ${user?.email}"
    binding.textViewFullName.text = "Nome Completo: ${user?.firstname} ${user?.lastname}"
    binding.textViewSubtitleBooks.text = "Livros de ${user?.firstname}"
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
    val like = book.book_id?.let {
      Like(UUID.randomUUID().toString(), loggedUser.getUser()?.user_id!!, book.owner!!,
        it, Date(), null)
    }
//    likeDAO.insert(like)
//    matchDAO.likeMacth(like)
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