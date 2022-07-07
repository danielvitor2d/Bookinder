package com.mobile.bookinder.common.dao

import android.util.Log
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.User
import java.util.*

class BookDAO {

  fun insert(new_book: Book, user: User): Boolean{
    val userDAO = UserDAO()
    for (book in bookList) {
      if (new_book.book_id == book.book_id)
        return false
    }
    userDAO.insertBook(user, new_book.book_id.toString())
    bookList.add(new_book)

    return true
  }

  fun find(title_: String): MutableList<Book>{
    val findBooks: MutableList<Book> = mutableListOf()
    for (book in bookList) {
      if (book.title.equals(title_))
        findBooks.add(book)
    }
    return findBooks
  }

  fun findId(id: String?): Book?{
    for (book in bookList) {
      if (book.book_id == id)
        return book
    }
    return null
  }

  fun alterBook(book: Book, title: String, author: String, gender: String, synopsis: String, photos: MutableList<String>){
    book.title = title
    book.author = author
    book.gender = gender
    book.synopsis = synopsis
    book.photos = photos
  }

  fun allByUser(user_id: String?): MutableList<Book>{
    return bookList.filter { it.owner == user_id } as MutableList<Book>
  }

  fun addPhoto(book_id: String, photo_id: String){
    val book = findId(book_id)
    book?.photos?.add(photo_id)
  }

  fun allExcludeUserBooks(user: User?): MutableList<Book>{
    val user_books = user?.books //lista de uuid
    val books: MutableList<Book> = mutableListOf()

    for (book in bookList){
      if(user_books?.contains(book.book_id.toString()) == false){
        books.add(book)
      }
    }
    return books
  }

  fun setBook(book_id: String?, _book: Book) {
    for (i in 0 until bookList.size) {
      if (bookList[i].book_id == book_id) {
        bookList[i] = _book
        return
      }
    }
  }

  companion object {
    val bookList: MutableList<Book> = mutableListOf()
  }
}