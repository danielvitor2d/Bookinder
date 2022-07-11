package com.mobile.bookinder.screens.feed

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.LikeDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.interfaces.CardBookEvent
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser

class BookAdapter(private val feedCardBookEvent: FeedCardBookEvent,
                  options: FirestoreRecyclerOptions<Book>
) : FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>(options) {
  private val storage = Firebase.storage
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

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int, book: Book) {
    holder.bookTitle.text = "Livro: ${book.title}"
    holder.bookAuthor.text = "Autor(a): ${book.author}"
    holder.gender.text = "Gênero: ${book.gender}"

    if (book.photos?.size!! > 0) {
      val imageUrl = book.photos?.get(0)

      val storageRef = storage.reference
      val imageRef = imageUrl?.let { storageRef.child(it) }

      imageRef?.downloadUrl?.addOnSuccessListener {
        Glide.with(holder.itemView)
          .load(it.toString())
          .centerCrop()
          .into(holder.coverPhoto)
      }
    }
    //nao temos o like ainda
//    val user_id = user?.user_id
//    val book_id = books[position].book_id
//    if (booksILiked.contains(book_id)){
//      holder.likeBook.setImageResource(R.drawable.ic_filled_star)
//    }

    holder.likeBook.setOnClickListener {
//      booksILiked = likeDAO.booksILiked(loggedUser.getUser()?.user_id)
//      val check = booksILiked.contains(books[position].book_id)
//      if (!check){//se nunca curti, agr curto
//        feedCardBookEvent.likeBook(books[position], position)
//        holder.likeBook.setImageResource(R.drawable.ic_filled_star)
//      }else{//entao deslike
//        val like = book_id?.let { it1 -> likeDAO.findLike(user_id!!, it1) }
//        feedCardBookEvent.deslikeBook(books[position], position, like)
//        holder.likeBook.setImageResource(R.drawable.ic_star)
//      }
    }

    holder.card.setOnClickListener {
      feedCardBookEvent.showCardBook(book, position)
    }

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