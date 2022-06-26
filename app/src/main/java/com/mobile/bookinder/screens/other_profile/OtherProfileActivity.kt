package com.mobile.bookinder.screens.other_profile

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivityOtherProfileBinding
import java.util.*

class OtherProfileActivity: AppCompatActivity(), CardBookEvent {

  private lateinit var binding: ActivityOtherProfileBinding
  private lateinit var books: MutableList<Book>
  private lateinit var bookView: RecyclerView
  private val userDAO = UserDAO()
  private val bookDAO = BookDAO()
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
  }

  override fun onResume() {
    super.onResume()

    updateRecyclerView()
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
    // Implementar pra abrir tela de livro de outra pessoa
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