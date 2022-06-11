package com.mobile.bookinder.screens.my_books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R

class ItemAdapterBook: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val books: MutableList<String> =
    mutableListOf(
      "Livro: As aventuras de Al Samir",
      "Livro: Fomin",
      "Livro: A culpa é das estrelas",
      "Livro: O homem de giz",
      "Livro: Nárnia",
      "Livro: As aventuras de Al Samir",
      "Livro: Fomin",
      "Livro: A culpa é das estrelas",
      "Livro: O homem de giz",
      "Livro: Nárnia",
      "Livro: As aventuras de Al Samir",
      "Livro: Fomin",
      "Livro: A culpa é das estrelas",
      "Livro: O homem de giz",
      "Livro: Nárnia")

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_my_books, parent, false)
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
    val bookMessage: TextView = itemView.findViewById(R.id.bookMessage)
  }
}