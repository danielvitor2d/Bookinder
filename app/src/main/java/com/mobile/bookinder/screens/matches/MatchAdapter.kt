package com.mobile.bookinder.screens.matches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R

class MatchAdapter(private val clickListener: (String, Int) -> Unit): RecyclerView.Adapter<MatchAdapter.MessageViewHolder>() {
  private val matches: MutableList<String> =
    mutableListOf(
      "Você deu match com Eduardo!",
      "Você deu match com Douglas!",
      "Você deu match com Letícia!",
      "Você deu match com Jonas!",
      "Você deu match com Érica!",
      "Você deu match com Vitória!")

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val card = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.message_card_matches, parent, false)
    return MessageViewHolder(card) {
      clickListener(matches[it], it)
    }
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    matches[position].also {
      holder.matchMessage.text = it
    }
  }

  override fun getItemCount(): Int {
    return matches.size
  }

  class MessageViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
    val matchMessage: TextView = itemView.findViewById(R.id.matchMessage)

    init {
      itemView.setOnClickListener {
        clickAtPosition(adapterPosition)
      }
    }
  }
}