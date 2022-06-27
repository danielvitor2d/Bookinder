package com.mobile.bookinder.screens.likes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.Util
import com.mobile.bookinder.screens.other_profile.OtherProfileActivity

class LikeAdapter(private val likes: MutableList<Like>): RecyclerView.Adapter<LikeAdapter.MessageViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_likes, parent, false)
    return MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    val userDAO = UserDAO()
    val bookDAO = BookDAO()

    val userFrom = userDAO.getById(likes[position].user_id_from)
    val book = bookDAO.findId(likes[position].book_id_to)
    val formattedDate = Util.getFormattedDate(likes[position].date)
    val formattedHour = Util.getFormattedHour(likes[position].date)

    "${userFrom?.firstname} curtiu seu livro '${book?.title}'".also { holder.textViewLikeMessageOne.text = it }
    "Veja se algum dos livros de ${userFrom?.firstname} lhe interessa".also { holder.textViewLikeMessageTwo.text = it }
    "$formattedHour - $formattedDate".also { holder.textViewLikeDateTime.text = it }

    holder.textViewGoToProfile.setOnClickListener {
      Toast.makeText(holder.itemView.context, "Indo para perfil de ${userFrom?.firstname}", Toast.LENGTH_SHORT).show()
      var intent = Intent(it.context, OtherProfileActivity::class.java)

      var bundle = Bundle()
      bundle.putString("user_id", userFrom?.user_id.toString())

      intent.putExtras(bundle)

      it.context.startActivity(intent)
    }
  }

  override fun getItemCount(): Int {
    return likes.size
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textViewLikeDateTime: TextView = itemView.findViewById(R.id.textViewLikeDateTime)
    val textViewLikeMessageOne: TextView = itemView.findViewById(R.id.textViewLikeMessageOne)
    val textViewLikeMessageTwo: TextView = itemView.findViewById(R.id.textViewLikeMessageTwo)
    val textViewGoToProfile: ImageView = itemView.findViewById(R.id.textViewGoToProfile)
  }
}