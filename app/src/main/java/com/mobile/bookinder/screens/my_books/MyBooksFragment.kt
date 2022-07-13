package com.mobile.bookinder.screens.my_books

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
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
  private lateinit var adapter: BookAdapter

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

    val query: Query = db
      .collection("books")
      .orderBy("book_id")
      .limit(50)
      .whereEqualTo("owner", auth.currentUser?.uid)

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
    itemList.layoutManager = WrapContentLinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
    itemList.adapter = adapter

    binding.register.setOnClickListener {
      contractLauncher.launch(Book())
    }
  }

  private fun actionRemoveBook(context: Context, book: Book, position: Int) {
    Log.d("Book: ", "$book $position")

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

      adapter.deleteItem(position)

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

  private val contractLauncher = registerForActivityResult(MyContract()) { book: Book? ->
    if (book == null) {
      return@registerForActivityResult
    }

    adapter.notifyItemInserted(adapter.itemCount-1)
  }

  override fun onStart() {
    super.onStart()
    adapter.startListening()
  }

  override fun onStop() {
    super.onStop()
    adapter.stopListening()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun removeCardBook(book: Book, position: Int) {
    actionRemoveBook(binding.root.context, book, position)
  }

  override fun showCardBook(book: Book, position: Int) {
    openBookPage(book)
  }
}

class MyContract: ActivityResultContract<Book, Book>() {
  override fun createIntent(context: Context, request: Book) =
    Intent(context, RegisterBook::class.java).apply {
      putExtra("book", request)
    }

  override fun parseResult(resultCode: Int, intent: Intent?): Book? {
    if (resultCode != Activity.RESULT_OK) {
      return null
    }

    return intent?.getSerializableExtra(RECEIVE_CODE) as Book
  }

  companion object {
    const val SEND_CODE = "send_code"
    const val RECEIVE_CODE = "receive_code"
  }
}