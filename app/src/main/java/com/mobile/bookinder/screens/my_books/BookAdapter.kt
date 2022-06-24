package com.mobile.bookinder.screens.my_books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

  fun removeItem(positionBook: Int) {
    if (positionBook >= books.size) return
    val book = books[positionBook]
    if (bookDao.remove(book.book_id)) {
      books.removeAt(positionBook)
      notifyItemRemoved(positionBook)
    }
  }

  fun updateAll() {
    books = bookDao.allByUser(loggedUser.getUser()?.user_id)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    books = bookDao.allByUser(loggedUser.getUser()?.user_id)
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