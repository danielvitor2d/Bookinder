package com.mobile.bookinder.screens.feed

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.databinding.FragmentFeedBinding

class FeedFragment: Fragment() {
  private var _binding: FragmentFeedBinding? = null
  private val binding get() = _binding!!
  private lateinit var bookAdapter: BookAdapter
  private lateinit var imageButtonLikeBook: ImageView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFeedBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)

    return binding.root;
  }

  private fun setUpRecyclerView(view: View) {
    val itemList = view.findViewById<RecyclerView>(R.id.itemListMyFeed)
    itemList.layoutManager = LinearLayoutManager(view.context)
    bookAdapter = BookAdapter() { book, pos ->
      actionLikeBook(binding.root.context, book, pos)
    }
    itemList.adapter = bookAdapter
  }

  override fun onResume() {
    super.onResume()
    bookAdapter.updateAll()
  }

  private fun actionLikeBook(context: Context, book: Book, pos: Int) {
    Toast.makeText(context, "Curtiu ${book.title}!", Toast.LENGTH_SHORT).show()
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}