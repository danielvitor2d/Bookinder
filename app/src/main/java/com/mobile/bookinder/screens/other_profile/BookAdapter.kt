package com.mobile.bookinder.screens.other_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.model.Book

class BookAdapter(private val books: MutableList<Book>, private val cardBookEvent: CardBookEvent): RecyclerView.Adapter<BookAdapter.MessageViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_feed_books, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    "Livro: ${books[position].title}".also { holder.title.text = it }
    "Autor(a): ${books[position].author}".also { holder.author.text = it }

    holder.card.setOnClickListener {
      cardBookEvent.showCardBook(books[position], position)
    }
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author)
    val card: CardView = itemView.findViewById(R.id.cardSelect)
  }
}