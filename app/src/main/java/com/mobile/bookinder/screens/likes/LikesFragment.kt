package com.mobile.bookinder.screens.likes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.FragmentLikesBinding

class LikesFragment: Fragment() {
  private var _binding: FragmentLikesBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentLikesBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)
    return binding.root
  }

  private fun setUpRecyclerView(view: View) {
    val likesList = view.findViewById<RecyclerView>(R.id.likesList)
    likesList.layoutManager = LinearLayoutManager(view.context)
    likesList.adapter = LikeAdapter()
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}