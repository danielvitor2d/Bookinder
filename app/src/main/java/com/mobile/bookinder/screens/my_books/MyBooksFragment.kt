package com.mobile.bookinder.screens.my_books

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mobile.bookinder.common.interfaces.CardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.databinding.FragmentMyBooksBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class MyBooksFragment : Fragment(), CardBookEvent {
  private var _binding: FragmentMyBooksBinding? = null
  private val binding get() = _binding!!

  private lateinit var itemList: RecyclerView
  private var adapter: FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>? = null

  private var storage = Firebase.storage
  private var db = Firebase.firestore
  private var auth = Firebase.auth

  private var storageRef = storage.reference
  private var imagesRef = storageRef.child("images")

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {

    var userId = ""
    GlobalScope.launch {
      val task = db.collection("users").whereEqualTo("email", auth.currentUser?.email)
        .get().await()
      userId = task.documents[0].data?.get("user_id") as String
    }

    val query: Query = FirebaseFirestore.getInstance()
      .collection("books")
      .orderBy("book_id")
      .limit(50)
      .whereEqualTo("owner", userId)

    val options: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>()
      .setQuery(query, Book::class.java)
      .build()

    adapter = BookAdapter(this, options)

    _binding = FragmentMyBooksBinding.inflate(inflater, container, false)

    setUpRecyclerView(binding.root)

    return binding.root
  }

  private fun setUpRecyclerView(view: View) {
    itemList = view.findViewById(R.id.itemListMyBooks)
    itemList.layoutManager = LinearLayoutManager(view.context)
    itemList.adapter = adapter

    binding.register.setOnClickListener {
      val intent = Intent(this.context, RegisterBook::class.java)
      startActivity(intent);
    }
  }

  private fun actionRemoveBook(context: Context, book: Book) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle("Deseja remover o livro ${book.title}?")

    alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
      for (photo in book.photos!!) {
        try {
          storageRef.child(photo).delete()
        } catch (ex: Exception) {
          ex.message?.let { Log.d("Error: ", it) }
        }
      }

      val bookId = book.book_id as String

      db.collection("books")
        .document(bookId)
        .delete()

      adapter?.notifyDataSetChanged()

      dialog.dismiss()
    }

    alertDialogBuilder.setNegativeButton("Não") { dialog, _ ->
      dialog.dismiss()
    }

    alertDialogBuilder.show()
  }

  private fun openBookPage(book: Book) {
    val bundle = Bundle()
    bundle.putSerializable("book", book)

    val intent = Intent(this.context, MyBookActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  override fun onStart() {
    super.onStart()
    adapter?.startListening()
  }

  override fun onDestroy() {
    super.onDestroy()
    adapter?.stopListening()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun removeCardBook(book: Book, position: Int) {
    actionRemoveBook(binding.root.context, book)
  }

  override fun showCardBook(book: Book, position: Int) {
    openBookPage(book)
  }
}