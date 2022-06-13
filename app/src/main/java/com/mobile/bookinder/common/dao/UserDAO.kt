package com.mobile.bookinder.common.dao

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

  fun setUser(user_id: UUID?, _user: User) {
    for (i in 0 until userList.size) {
      if (userList[i].user_id == user_id) {
        userList[i] = _user
        return
      }
    }
  }

  companion object {
    val userList: MutableList<User> = mutableListOf()
  }
}