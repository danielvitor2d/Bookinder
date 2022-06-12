package com.mobile.bookinder.screens.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    return binding.root
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}