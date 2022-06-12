package com.mobile.bookinder.screens.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.FragmentFeedBinding

class FeedFragment: Fragment() {
  private var _binding: FragmentFeedBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentFeedBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)
    return binding.root;
  }

  private fun setUpRecyclerView(view: View) {
    val itemList = view.findViewById<RecyclerView>(R.id.itemListMyFeed)
    itemList.layoutManager = LinearLayoutManager(view.context)
    itemList.adapter = BookAdapter()
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}