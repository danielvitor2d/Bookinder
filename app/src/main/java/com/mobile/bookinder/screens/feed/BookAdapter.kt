package com.mobile.bookinder.screens.feed

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser

class BookAdapter(private val clickListener: (Book, Int) -> Unit): RecyclerView.Adapter<BookAdapter.MessageViewHolder>() {
  private val bookDao = BookDAO()
  private var books: MutableList<Book> = mutableListOf()
  private val loggedUser = LoggedUser()
  private val user = loggedUser.getUser()

  fun updateAll() {
    books = bookDao.all(user)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    books = bookDao.all(user)
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_feed_books, parent, false)
    return MessageViewHolder(card) {
      clickListener(books[it], it)
    }
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    holder.bookTitle.text = "Livro: ${books[position].title}"
    holder.bookAuthor.text = "Autor(a): ${books[position].author}"
    holder.gender.text = "Gênero: ${books[position].author}"

    val photoUuid = books[position].photos[0]

    if(photoUuid != null){
      val photoDAO = PhotoDAO()
      val photo = photoDAO.findById(photoUuid)
      val myBitmap = BitmapFactory.decodeFile(photo?.path)
      holder.coverPhoto.setImageBitmap(myBitmap)
    }
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
    private var toggleLiked = true
    val bookTitle: TextView = itemView.findViewById(R.id.title)
    val bookAuthor: TextView = itemView.findViewById(R.id.author)
    val gender: TextView = itemView.findViewById(R.id.gender)
    val coverPhoto: ImageView = itemView.findViewById(R.id.coverPhoto)
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