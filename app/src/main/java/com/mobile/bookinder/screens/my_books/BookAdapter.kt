package com.mobile.bookinder.screens.my_books

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.model.Book


class BookAdapter(private val books: MutableList<Book>, private val clickListener: (Book, Int) -> Unit): RecyclerView.Adapter<BookAdapter.MessageViewHolder>() {
  public fun removeItem(positionBook: Int) {
    notifyItemRemoved(positionBook)
  }

  public fun updateAll(){
    notifyDataSetChanged()
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
    holder.title.text = "Livro: ${books[position].title}"
    holder.author.text = "Autor(a): ${books[position].author}"
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author)

    init {
      itemView.findViewById<ImageButton>(R.id.imageButtonRemoveBook).setOnClickListener {
        clickAtPosition(adapterPosition)
      }
      itemView.setOnClickListener {
        clickAtPosition(adapterPosition)
      }
    }
  }
}