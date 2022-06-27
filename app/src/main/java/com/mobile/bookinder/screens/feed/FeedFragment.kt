package com.mobile.bookinder.screens.feed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.LikeDAO
import com.mobile.bookinder.common.dao.MatchDAO
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.databinding.FragmentFeedBinding
import com.mobile.bookinder.screens.my_books.BookActivity
import java.util.*
import java.util.Date

class FeedFragment: Fragment(), FeedCardBookEvent {
  private var _binding: FragmentFeedBinding? = null
  private val binding get() = _binding!!
  private lateinit var bookAdapter: BookAdapter

  private val bookDao = BookDAO()
  private val likeDAO = LikeDAO()
  private val matchDAO = MatchDAO()
  private val loggedUser = LoggedUser()

  private lateinit var books: MutableList<Book>
  private lateinit var itemList: RecyclerView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFeedBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)

    return binding.root;
  }

  private fun setUpRecyclerView(view: View) {
    itemList = view.findViewById<RecyclerView>(R.id.itemListMyFeed)
    itemList.layoutManager = LinearLayoutManager(view.context)

  }

  override fun onResume() {
    super.onResume()

    updateRecicleView()
  }

  private fun actionLikeBook(context: Context, book: Book, pos: Int) {
    Toast.makeText(context, "Curtiu ${book.title}!", Toast.LENGTH_SHORT).show()
  }

  private fun openBookPage(book: Book){
    val bundle = Bundle()
    bundle.putString("book_id", book.book_id.toString())

    val intent = Intent(this.context, BookActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
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

  private fun updateRecicleView(){
    val currentUser = loggedUser.getUser()
    if (currentUser == null)
      return

    books = bookDao.allExcludeUserBooks(currentUser)
    itemList.adapter = BookAdapter(books, this)
  }
}