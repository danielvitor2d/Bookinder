package com.mobile.bookinder.screens.dao

import android.os.Parcelable
import com.mobile.bookinder.common.User

class UserDAO{
  fun insert(new_user: User): Boolean{
    for(user in userList) {
      if(new_user.email == user.email)
        return false
    }
    new_user.user_id = newId()
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

  fun find_by_id(id: Int): User? {
    for(user in userList) {
      if(user.user_id == id)
        return user
    }
    return null
  }


  fun newId(): Int {
    return userList.size+1
  }

  companion object {
    val userList: MutableList<User> = mutableListOf()
  }
}