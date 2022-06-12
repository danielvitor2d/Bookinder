package com.mobile.bookinder.screens.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R

class BookAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val books: MutableList<String> =
    mutableListOf(
      "Livro: As crônicas de Nárnia",
      "Livro: O senhor dos Anéis",
      "Livro: As Crônicas de Gelo e Fogo",
      "Livro: A cidade de vidro",
      "Livro: Trono de ferro",
      "Livro: Percy Jackson e os Olimpianos")

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_feed_books, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = books[position]
    if (holder is MessageViewHolder) {
      holder.bookMessage.text = currentItem
    }
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val bookMessage: TextView = itemView.findViewById(R.id.likeMessage)
  }
}