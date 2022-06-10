package com.mobile.bookinder.screens.my_books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.FragmentMyBooksBinding
import com.mobile.bookinder.databinding.FragmentProfileBinding

class MyBooksFragment: Fragment() {
  private var _binding: FragmentMyBooksBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentMyBooksBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}