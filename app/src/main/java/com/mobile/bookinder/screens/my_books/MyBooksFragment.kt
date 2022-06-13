package com.mobile.bookinder.screens.my_books

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.FragmentMyBooksBinding
import com.mobile.bookinder.screens.dao.UserDAO
import java.util.logging.Logger

class MyBooksFragment: Fragment() {
  private var _binding: FragmentMyBooksBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val user_id = this.activity?.intent?.getIntExtra("user_id", -1) ?: -1

    _binding = FragmentMyBooksBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)
    binding.buttonBookRegister.setOnClickListener{
      val intent = Intent(this.context, BookRegister::class.java)
      intent.putExtra("user_id", user_id)

      startActivity(intent)
    }
    return binding.root
  }

  private fun setUpRecyclerView(view: View) {
    val itemList = view.findViewById<RecyclerView>(R.id.itemListMyBooks)
    itemList.layoutManager = LinearLayoutManager(view.context)
    itemList.adapter = BookAdapter()

  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}