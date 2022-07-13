package com.mobile.bookinder.screens.likes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.mobile.bookinder.R
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.common.model.Util
import com.mobile.bookinder.screens.other_profile.OtherProfileActivity

class LikeAdapter(options: FirestoreRecyclerOptions<Like>) : FirestoreRecyclerAdapter<Like, LikeAdapter.MessageViewHolder>(options) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_likes, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Like) {
    FirebaseFirestore.getInstance()
      .collection("users")
      .whereEqualTo("user_id", model.user_id_from)
      .get()
      .addOnSuccessListener {
        holder.user = it.documents[0].toObject<User>()

        updateText(holder)
      }

    FirebaseFirestore.getInstance()
      .collection("books")
      .whereEqualTo("book_id", model.book_id_to)
      .get()
      .addOnSuccessListener {
        holder.book = it.documents[0].toObject<Book>()

        updateText(holder)
      }

    val formattedDate = Util.getFormattedDate(model.date!!)
    val formattedHour = Util.getFormattedHour(model.date!!)

    "$formattedHour - $formattedDate".also { holder.textViewLikeDateTime.text = it }

    holder.textViewGoToProfile.setOnClickListener {
      Toast.makeText(holder.itemView.context, "Indo para perfil de ${holder.user?.firstname}", Toast.LENGTH_SHORT).show()
      var intent = Intent(it.context, OtherProfileActivity::class.java)

      var bundle = Bundle()
      bundle.putString("user_id", holder.user?.user_id.toString())

      intent.putExtras(bundle)

      it.context.startActivity(intent)
    }
  }

  private fun updateText(holder: MessageViewHolder){
    val first_name = holder.user?.firstname
    val book_title = holder.book?.title
    holder.textViewLikeMessageOne.text = "${first_name} curtiu seu livro '${book_title}'"
    holder.textViewLikeMessageTwo.text = "Veja se algum dos livros de ${first_name} lhe interessa"
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textViewLikeDateTime: TextView = itemView.findViewById(R.id.textViewLikeDateTime)
    val textViewLikeMessageOne: TextView = itemView.findViewById(R.id.textViewLikeMessageOne)
    val textViewLikeMessageTwo: TextView = itemView.findViewById(R.id.textViewLikeMessageTwo)
    val textViewGoToProfile: ImageView = itemView.findViewById(R.id.textViewGoToProfile)
    var user : User? = null
    var book : Book? = null
  }
}