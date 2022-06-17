package com.mobile.bookinder.screens.my_books

import android.app.AlertDialog
import android.content.Context
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

class MyBooksFragment: Fragment() {
  private var _binding: FragmentMyBooksBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMyBooksBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)
    return binding.root
  }

  private fun setUpRecyclerView(view: View) {
    val itemList = view.findViewById<RecyclerView>(R.id.itemListMyBooks)
    itemList.layoutManager = LinearLayoutManager(view.context)
    itemList.adapter = BookAdapter() { book, pos ->
      val adapter = itemList.adapter
      if (adapter !is BookAdapter) return@BookAdapter
      actionRemoveBook(binding.root.context, adapter, book, pos)
    }
  }

  private fun actionRemoveBook(context: Context, bookAdapter: BookAdapter, book: String, pos: Int) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle("Deseja remover o livro $book?")
    alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
      bookAdapter.removeItem(pos)
      dialog.dismiss()
    }
    alertDialogBuilder.setNegativeButton("Não") { dialog, _ ->
      dialog.dismiss()
    }
    alertDialogBuilder.show()
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}