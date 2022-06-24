package com.mobile.bookinder.screens.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser

class BookAdapter(private val clickListener: (Book, Int) -> Unit): RecyclerView.Adapter<BookAdapter.MessageViewHolder>() {
  private val bookDao = BookDAO()
  private var books: MutableList<Book> = mutableListOf()
  private val loggedUser = LoggedUser()

  fun updateAll() {
    books = bookDao.allByUser(loggedUser.getUser()?.user_id)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    books = bookDao.all()
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_feed_books, parent, false)
    return MessageViewHolder(card) {
      clickListener(books[it], it)
    }
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    "Livro: ${books[position].title}".also { holder.bookTitle.text = it }
    "Autor(a): ${books[position].author}".also { holder.bookAuthor.text = it }
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
    private var toggleLiked = true
    val bookTitle: TextView = itemView.findViewById(R.id.title)
    val bookAuthor: TextView = itemView.findViewById(R.id.author)
    private val imageButtonLikeBook: ImageView = itemView.findViewById(R.id.imageButtonLikeBook)

    init {
      imageButtonLikeBook.setOnClickListener {
        clickAtPosition(adapterPosition)
        if (toggleLiked) imageButtonLikeBook.setImageResource(R.drawable.ic_filled_star)
        else imageButtonLikeBook.setImageResource(R.drawable.ic_star)
        toggleLiked = !toggleLiked
      }
    }
  }
}