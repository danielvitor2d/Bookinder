package com.mobile.bookinder.screens.my_books

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.databinding.FragmentMyBooksBinding

class MyBooksFragment: Fragment() {
  private var _binding: FragmentMyBooksBinding? = null
  private val binding get() = _binding!!
  private var bookAdapter: BookAdapter? = null
  private val bookDao = BookDAO()
  private val loggedUser = LoggedUser()

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

    val books = bookDao.all()

    bookAdapter = BookAdapter(books) { book, _ ->
      actionRemoveBook(binding.root.context, bookAdapter, book)
    }

    itemList.adapter = bookAdapter

    binding.register.setOnClickListener {
      val intent = Intent(this.context, RegisterBook::class.java)
      startActivity(intent)
    }
  }

  override fun onResume() {
    super.onResume()
    bookAdapter?.updateAll()
  }

  private fun actionRemoveBook(context: Context, bookAdapter: BookAdapter?, book: Book) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle("Deseja remover o livro ${book.title}?")
    alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
      bookDao.removeBook(book, loggedUser.getUser())
      bookAdapter?.removeItem(bookDao.positionBook(book))
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