package com.mobile.bookinder.screens.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book

class BookAdapter(private val feedCardBookEvent: FeedCardBookEvent,
                  options: FirestoreRecyclerOptions<Book>
) : FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>(options) {
  private val storage = Firebase.storage
  private val db = Firebase.firestore
  private val auth = Firebase.auth


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_feed_books, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int, book: Book) {
    "Livro: ${book.title}".also { holder.bookTitle.text = it }
    "Autor(a): ${book.author}".also { holder.bookAuthor.text = it }
    "Gênero: ${book.gender}".also { holder.gender.text = it }

    if (book.photos?.size!! > 0) {
      val imageUrl = book.photos?.get(0)

      val storageRef = storage.reference
      val imageRef = imageUrl?.let { storageRef.child(it) }

      imageRef?.downloadUrl?.addOnSuccessListener {
        Glide.with(holder.itemView)
          .load(it.toString())
          .centerCrop()
          .into(holder.coverPhoto)
      }
    }

    holder.likeBook.setImageResource(R.drawable.ic_star)

    db.collection("likes")
      .whereEqualTo("user_id_from", auth.currentUser?.uid)
      .whereEqualTo("book_id_to", book.book_id)
      .get()
      .addOnSuccessListener { task ->
        holder.liked = (task.documents.size > 0)

        if (holder.liked){
          holder.likeBook.setImageResource(R.drawable.ic_filled_star)
        } else {
          holder.likeBook.setImageResource(R.drawable.ic_star)
        }
      }


    holder.likeBook.setOnClickListener {
      if (holder.liked){
        feedCardBookEvent.deslikeBook(book, position, null)
      }else{
        feedCardBookEvent.likeBook(book, position)
      }
    }

    holder.card.setOnClickListener {
      feedCardBookEvent.showCardBook(book, position)
    }
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val card: CardView = itemView.findViewById(R.id.cardSelect)
    val bookTitle: TextView = itemView.findViewById(R.id.title)
    val bookAuthor: TextView = itemView.findViewById(R.id.author)
    val gender: TextView = itemView.findViewById(R.id.gender)
    val coverPhoto: ImageView = itemView.findViewById(R.id.coverPhoto)
    val likeBook: ImageButton = itemView.findViewById(R.id.imageButtonLikeBook)
    var liked = false
  }
}