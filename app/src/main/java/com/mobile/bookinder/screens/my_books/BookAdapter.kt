package com.mobile.bookinder.screens.my_books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.interfaces.CardBookEvent
import com.mobile.bookinder.common.model.Book

class BookAdapter(
  private val cardBookEvent: CardBookEvent,
  options: FirestoreRecyclerOptions<Book>,
) : FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>(options) {
  private val storage = Firebase.storage

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_my_books, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int, book: Book) {
    "Livro: ${book.title}".also { holder.title.text = it }
    "Autor(a): ${book.author}".also { holder.author.text = it }

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

    holder.removeButton.setOnClickListener {
      cardBookEvent.removeCardBook(book, position)
    }

    holder.card.setOnClickListener {
      cardBookEvent.showCardBook(book, position)
    }
  }

  class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author)
    val card: CardView = itemView.findViewById(R.id.cardSelect)
    val coverPhoto: ImageView = itemView.findViewById(R.id.coverPhoto)
    val removeButton: ImageButton = itemView.findViewById(R.id.imageButtonRemoveBook)
  }
}