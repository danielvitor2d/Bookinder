package com.mobile.bookinder.screens.my_books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.model.Book

class BookAdapter(private val books: MutableList<Book>, private val clickListener: (Book, Int) -> Unit): RecyclerView.Adapter<BookAdapter.MessageViewHolder>() {
  fun removeItem(positionBook: Int) {
    books.removeAt(positionBook)
    notifyItemRemoved(positionBook)
  }

  fun updateAll() = notifyDataSetChanged()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_my_books, parent, false)
    return MessageViewHolder(card) {
      clickListener(books[it], it)
    }
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    "Livro: ${books[position].title}".also { holder.bookMessage.text = it }
    "Autor(a): ${books[position].author}".also { holder.messageDiscipline.text = it }
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
    val bookMessage: TextView = itemView.findViewById(R.id.title)
    val messageDiscipline: TextView = itemView.findViewById(R.id.author)

    init {
      itemView.findViewById<ImageButton>(R.id.imageButtonRemoveBook).setOnClickListener {
        clickAtPosition(adapterPosition)
      }
    }
  }
}