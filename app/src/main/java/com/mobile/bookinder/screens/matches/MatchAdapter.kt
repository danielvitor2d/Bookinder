package com.mobile.bookinder.screens.matches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R

class MatchAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val matches: MutableList<String> =
    mutableListOf(
      "Você deu match com Eduardo!",
      "Você deu match com Douglas!",
      "Você deu match com Letícia!",
      "Você deu match com Jonas!",
      "Você deu match com Érica!",
      "Você deu match com Vitória!")

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_matches, parent, false)
    return MatchAdapter.MessageViewHolder(card)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = matches[position]
    if (holder is MatchAdapter.MessageViewHolder) {
      holder.matchMessage.text = currentItem
    }
  }

  override fun getItemCount(): Int {
    return matches.size
  }

  class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val matchMessage: TextView = itemView.findViewById(R.id.matchMessage)
  }
}