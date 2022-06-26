package com.mobile.bookinder.common.dao

import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.common.model.User
import java.util.*

class UserDAO {
  fun insert(new_user: User): Boolean{
    for (user in userList) {
      if (new_user.email == user.email)
        return false
    }
    userList.add(new_user)

    return true
  }

  fun find(email: String, password: String): User? {
    for (user in userList) {
      if (user.email == email && user.password == password)
        return user
    }
    return null
  }

  fun find(user_id: UUID): Boolean{ //saber se o usuário existe
    for (user in userList){
      if (user.user_id == user_id){
        return true
      }
    }
    return false
  }

  fun insertPhoto(user: User, photo: Photo): Boolean{
    if (find(user.user_id)){
      user.photo_id = photo.photo_id
      return true
    }
    return false
  }

  fun insertBook(user: User?, book_id: UUID?): Boolean{
    val bookDAO = BookDAO()
    if (bookDAO.findId(book_id) != null) {
      return false
    }
    if (book_id != null) {
      user?.books?.add(book_id)
    }
    return true

  }

  fun setUser(user_id: UUID?, _user: User) {
    for (i in 0 until userList.size) {
      if (userList[i].user_id == user_id) {
        userList[i] = _user
        return
      }
    }
  }

  fun getById(user_id: UUID?): User? {
    for (user in userList) {
      if (user.user_id == user_id) {
        return user
      }
    }
    return null
  }

  fun emailExists(email: String): Boolean{
    for (user in userList){
      if(user.email == email)
        return true
    }
    return false
  }

  companion object {
    val userList: MutableList<User> = mutableListOf()
  }
}