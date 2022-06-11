package com.mobile.bookinder.screens.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.mobile.bookinder.databinding.FragmentProfileBinding
import com.mobile.bookinder.screens.feedback.Feedback

class ProfileFragment: Fragment() {

  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private lateinit var buttonVisualizarFeedbacks: Button

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentProfileBinding.inflate(inflater, container, false)

    buttonVisualizarFeedbacks = binding.buttonVisualizarFeedbacks

    setUpListeners(binding.root.context)

    return binding.root
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }

  private fun setUpListeners(context: Context) {
    buttonVisualizarFeedbacks.setOnClickListener {
      val intent = Intent(context, Feedback::class.java)
      startActivity(intent)
    }
  }
}