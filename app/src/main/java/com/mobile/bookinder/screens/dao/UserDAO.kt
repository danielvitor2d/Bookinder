package com.mobile.bookinder.screens.dao

import com.mobile.bookinder.common.User

class UserDAO {
  fun insert(new_user: User): Boolean{
    for(user in userList) {
      if(new_user.email == user.email)
        return false
    }
    userList.add(new_user)

    return true
  }

  fun find(email: String, password: String): User? {
    for(user in userList) {
      if(user.email == email && user.password == password)
        return user
    }
    return null
  }

  companion object {
    val userList: MutableList<User> = mutableListOf()
  }
}