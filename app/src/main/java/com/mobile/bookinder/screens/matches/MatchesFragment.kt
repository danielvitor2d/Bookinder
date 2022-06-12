package com.mobile.bookinder.screens.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.FragmentLikesBinding
import com.mobile.bookinder.databinding.FragmentMatchesBinding

class MatchesFragment: Fragment() {
  private var _binding: FragmentMatchesBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentMatchesBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)
    return binding.root
  }

  private fun setUpRecyclerView(view: View) {
    val matchesList = view.findViewById<RecyclerView>(R.id.matchesList)
    matchesList.layoutManager = LinearLayoutManager(view.context)
    matchesList.adapter = MatchAdapter()
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}