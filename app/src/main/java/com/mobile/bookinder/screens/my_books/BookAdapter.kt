package com.mobile.bookinder.screens.my_books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R

class BookAdapter(private val clickListener: (String, Int) -> Unit): RecyclerView.Adapter<BookAdapter.MessageViewHolder>() {
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

  public fun removeItem(position: Int) {
    books.removeAt(position)
    notifyItemRemoved(position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_my_books, parent, false)
    return MessageViewHolder(card) {
      clickListener(books[it], it)
    }
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    holder.bookMessage.text = books[position]
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
    val bookMessage: TextView = itemView.findViewById(R.id.likeMessage)

    init {
      itemView.findViewById<ImageButton>(R.id.imageButtonRemoveBook).setOnClickListener {
        clickAtPosition(adapterPosition)
      }
    }
  }
}