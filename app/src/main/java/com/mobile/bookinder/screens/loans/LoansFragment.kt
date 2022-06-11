package com.mobile.bookinder.screens.loans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobile.bookinder.databinding.FragmentLoansBinding

class LoansFragment: Fragment() {
  private var _binding: FragmentLoansBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentLoansBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}