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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

  private lateinit var itemList: RecyclerView
  private var adapter: FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>? = null

  private var storage = Firebase.storage
  private var db = Firebase.firestore
  private var auth = Firebase.auth

  private var storageRef = storage.reference

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    val query: Query = FirebaseFirestore.getInstance()
      .collection("books")
      .orderBy("book_id")
      .limit(50)
      .whereNotEqualTo("owner", auth.currentUser?.uid)

    val options: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>()
      .setQuery(query, Book::class.java)
      .build()

    adapter = BookAdapter(this, options)

    _binding = FragmentFeedBinding.inflate(inflater, container, false)

    setUpRecyclerView(binding.root)

    return binding.root;
  }

  private fun setUpRecyclerView(view: View) {
    itemList = view.findViewById(R.id.itemListMyFeed)
    itemList.layoutManager = LinearLayoutManager(view.context)
    itemList.adapter = adapter
  }

  private fun actionLikeBook(context: Context, book: Book, pos: Int) {
    Toast.makeText(context, "Curtiu ${book.title}!", Toast.LENGTH_SHORT).show()
  }

  private fun openBookPage(book: Book){
    val bundle = Bundle()
    bundle.putSerializable("book", book)

    val intent = Intent(this.context, BookActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  override fun onStart() {
    super.onStart()
    adapter?.startListening()
  }

  override fun onResume() {
    super.onResume()
    adapter?.notifyDataSetChanged()
  }

  override fun onDestroy() {
    super.onDestroy()
    adapter?.stopListening()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun showCardBook(book: Book, position: Int) {
    openBookPage(book)
  }

  override fun likeBook(book: Book, position: Int) {
//    likeDAO.insert(like)
//    matchDAO.likeMacth(like)
  }

  override fun deslikeBook(book: Book, position: Int, like: Like?) {
  }

}