package com.mobile.bookinder.screens.other_profile

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.LikeDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser

class BookAdapter(private val books: MutableList<Book>, private val cardBookEvent: FeedCardBookEvent): RecyclerView.Adapter<BookAdapter.MessageViewHolder>() {
    private var likeDAO = LikeDAO()
    private var loggedUser = LoggedUser()
    private var user = loggedUser.getUser()
    private var booksILiked = likeDAO.booksILiked(loggedUser.getUser()?.user_id)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_feed_books, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    holder.bookTitle.text = "Livro: ${books[position].title}"
    holder.bookAuthor.text = "Autor(a): ${books[position].author}"
    holder.gender.text = "Gênero: ${books[position].author}"

    val photoUuid = books[position].photos?.get(0)

    if(photoUuid != null){
      val photoDAO = PhotoDAO()
      val photo = photoDAO.findById(photoUuid)
      val myBitmap = BitmapFactory.decodeFile(photo?.path)
      holder.coverPhoto.setImageBitmap(myBitmap)
    }

    val user_id = user?.user_id
    val book_id = books[position].book_id
    if (booksILiked.contains(book_id)){
      holder.likeBook.setImageResource(R.drawable.ic_filled_star)
    }

    holder.likeBook.setOnClickListener {
      booksILiked = likeDAO.booksILiked(loggedUser.getUser()?.user_id)
      val check = booksILiked.contains(books[position].book_id)
      if (!check){//se nunca curti, agr curto
        cardBookEvent.likeBook(books[position], position)
        holder.likeBook.setImageResource(R.drawable.ic_filled_star)
      }else{//entao deslike
        val like = book_id?.let { it1 -> likeDAO.findLike(user_id!!, it1) }
        cardBookEvent.deslikeBook(books[position], position, like)
        holder.likeBook.setImageResource(R.drawable.ic_star)
      }

    }

    holder.card.setOnClickListener {
      cardBookEvent.showCardBook(books[position], position)
    }
  }

  override fun getItemCount(): Int {
    return books.size
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val card: CardView = itemView.findViewById(R.id.cardSelect)
    val bookTitle: TextView = itemView.findViewById(R.id.title)
    val bookAuthor: TextView = itemView.findViewById(R.id.author)
    val gender: TextView = itemView.findViewById(R.id.gender)
    val coverPhoto: ImageView = itemView.findViewById(R.id.coverPhoto)
    val likeBook: ImageButton = itemView.findViewById(R.id.imageButtonLikeBook)

  }
}