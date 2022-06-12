package com.mobile.bookinder.screens.likes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R

class LikeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val likes: MutableList<String> =
    mutableListOf(
      "Sabrina curtiu seu livro 'A menina que roubava livros'",
      "Bruno curtiu seu livro 'Nárnia'",
      "César curtiu seu livro 'As crônicas de Gelo e Fogo'",
      "Paula curtiu seu livro 'O menino do pijama listrado'",
      "Augusto curtiu seu livro 'A revolução dos bixos'",
      "Natália curtiu seu livro 'O homem de giz'")

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_likes, parent, false)
    return LikeAdapter.MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = likes[position]
    if (holder is LikeAdapter.MessageViewHolder) {
      holder.likeMessage.text = currentItem
    }
  }

  override fun getItemCount(): Int {
    return likes.size
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val likeMessage: TextView = itemView.findViewById(R.id.likeMessage)
  }
}