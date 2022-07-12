package com.mobile.bookinder.screens.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.LikeDAO
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.interfaces.CardBookEvent
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.LoggedUser
import kotlinx.coroutines.tasks.await
import java.util.*

class BookAdapter(private val feedCardBookEvent: FeedCardBookEvent,
                  options: FirestoreRecyclerOptions<Book>
) : FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>(options) {
  private val storage = Firebase.storage
  private val db = Firebase.firestore
  private val auth = Firebase.auth


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_feed_books, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int, book: Book) {
    "Livro: ${book.title}".also { holder.bookTitle.text = it }
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

    val likesQuery = db.collection("likes")
      .whereEqualTo("user_from", auth.currentUser?.uid)
      .get()
      .addOnCompleteListener{ task ->
        var likes: MutableList<Like>? = mutableListOf()
        var booksILiked: MutableList<String>? = mutableListOf()
        var removeBooks: MutableList<String>? = mutableListOf()

        if (task.isSuccessful){
          if (task.result.documents.size > 0){
            for (like in task.result.documents){
              val atual = like.toObject<Like>()
              likes?.add(atual!!)
              booksILiked?.add(atual?.book_id_to.toString())
            }
          }

          holder.likeBook.setOnClickListener {
            if (booksILiked?.contains(book.book_id.toString()) == true){
              holder.likeBook.setImageResource(R.drawable.ic_star)
              removeBooks?.add(book.book_id.toString())
              for (like in likes!!){
                if (like.book_id_to == book.book_id){
                  feedCardBookEvent.deslikeBook(book, position, like)
                  break
                }
              }

            }else{
              holder.likeBook.setImageResource(R.drawable.ic_filled_star)
              booksILiked?.add(book.book_id.toString())
              feedCardBookEvent.likeBook(book, position)
            }
          }
        }
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