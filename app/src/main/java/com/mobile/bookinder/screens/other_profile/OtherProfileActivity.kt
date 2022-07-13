package com.mobile.bookinder.screens.other_profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivityOtherProfileBinding
import com.mobile.bookinder.screens.my_books.BookActivity
import java.util.*

class OtherProfileActivity : AppCompatActivity(), FeedCardBookEvent {
  private val auth = Firebase.auth
  private val db = Firebase.firestore
  private val storage = Firebase.storage

  private var adapter: FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>? = null

  private lateinit var binding: ActivityOtherProfileBinding
  private lateinit var bookView: RecyclerView

  private var user: User? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityOtherProfileBinding.inflate(layoutInflater)

    val userId = intent.getStringExtra("user_id")

    val query: Query = FirebaseFirestore.getInstance()
      .collection("books")
      .whereEqualTo("owner", userId)

    val options: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>()
      .setQuery(query, Book::class.java)
      .build()

    adapter = BookAdapter(this, options)

    setUpRecyclerView(binding.root)

    setContentView(binding.root)
    setUpActionBar()

    db.collection("users")
      .document(userId as String)
      .get()
      .addOnSuccessListener { documentSnapshot ->
        user = documentSnapshot.toObject<User>()
        setUpTextViews()
      }
  }

  private fun openBookPage(book: Book) {
    val bundle = Bundle()
    bundle.putString("book_id", book.book_id.toString())

    val intent = Intent(binding.root.context, BookActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  private fun setUpTextViews() {
    "E-mail: ${user?.email}".also { binding.textViewEmail.text = it }
    "Nome Completo: ${user?.firstname} ${user?.lastname}".also {
      binding.textViewFullName.text = it
    }
    "Livros de ${user?.firstname}".also { binding.textViewSubtitleBooks.text = it }

    if (user?.photo?.length!! > 0) {
      val imagePath = user?.photo as String
      storage.reference.child(imagePath).downloadUrl.addOnSuccessListener {
        Glide.with(this)
          .load(it.toString())
          .centerCrop()
          .into(binding.imageViewPhotoUser)
      }
    }
  }

  private fun setUpRecyclerView(view: View) {
    bookView = view.findViewById(R.id.bookView)
    bookView.layoutManager = LinearLayoutManager(view.context)
    bookView.adapter = adapter
  }

  override fun showCardBook(book: Book, position: Int) {
    openBookPage(book)
  }

  override fun likeBook(book: Book, position: Int) {
    val like = Like(UUID.randomUUID().toString(),
      auth.currentUser?.uid.toString(),
      book.owner.toString(),
      book.book_id.toString())

    like.like_id?.let { id ->
      db.collection("likes").document(id).set(like).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          adapter?.notifyItemChanged(position)

          Toast.makeText(applicationContext, "Você curtiu ${book.title}", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(applicationContext, "Erro ao curtir livro", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  override fun deslikeBook(book: Book, position: Int, like: Like?) {
    db.collection("likes")
      .whereEqualTo("user_id_from", auth.currentUser?.uid)
      .whereEqualTo("book_id_to", book.book_id)
      .get()
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          for (document in task.result.documents)
            document.reference.delete()

          adapter?.notifyItemChanged(position)
          Toast.makeText(applicationContext, "Você descurtiu ${book.title}", Toast.LENGTH_SHORT)
            .show()
        } else {
          Toast.makeText(applicationContext, "Erro ao descurtir livro", Toast.LENGTH_SHORT).show()
        }
      }
  }

  private fun setUpActionBar() {
    setSupportActionBar(binding.toolbarFeedback)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Perfil"
  }

  override fun onStart() {
    super.onStart()
    adapter?.startListening()
  }

  override fun onStop() {
    super.onStop()
    adapter?.stopListening()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        finish()
        return true
      }
    }
    return super.onContextItemSelected(item)
  }

}