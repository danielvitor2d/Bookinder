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
import com.mobile.bookinder.common.interfaces.CardBookEvent
import com.mobile.bookinder.databinding.FragmentMyBooksBinding


class MyBooksFragment: Fragment(), CardBookEvent {
  private var _binding: FragmentMyBooksBinding? = null
  private val binding get() = _binding!!
  private val bookDao = BookDAO()
  private val loggedUser = LoggedUser()

  private lateinit var books: MutableList<Book>
  private lateinit var itemList: RecyclerView

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
    itemList = view.findViewById<RecyclerView>(R.id.itemListMyBooks)
    itemList.layoutManager = LinearLayoutManager(view.context)

    binding.register.setOnClickListener {
      val intent = Intent(this.context, RegisterBook::class.java)
      startActivity(intent);
    }

  }

  override fun onResume() {
    super.onResume()

    updateRecicleView()
  }

  private fun actionRemoveBook(context: Context, bookAdapter: BookAdapter, book: Book) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle("Deseja remover o livro ${book.title}?")

    alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
      bookDao.removeBook(book, loggedUser.getUser())
      updateRecicleView()

      dialog.dismiss()
    }

    alertDialogBuilder.setNegativeButton("Não") { dialog, _ ->
      dialog.dismiss()
    }

    alertDialogBuilder.show()
  }

  private fun openBookPage(book: Book){
    val bundle = Bundle()
    bundle.putString("book_id", book.book_id.toString())

    val intent = Intent(this.context, MyBookActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  private fun updateRecicleView(){
    val currentUser = loggedUser.getUser()
    if (currentUser == null)
      return

    books = bookDao.allByUser(currentUser.user_id)
    itemList.adapter = BookAdapter(books, this)
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }

  override fun removeCardBook(book: Book, position: Int) {
    actionRemoveBook(binding.root.context, itemList.adapter as BookAdapter, book)
  }

  override fun showCardBook(book: Book, position: Int) {
    openBookPage(book)
  }
}