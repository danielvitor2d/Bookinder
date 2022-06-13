package com.mobile.bookinder.common.model

class LoggedUser {
  fun login(_user: User) {
    user = _user
  }

  fun logout() {
    user = null
  }

  fun getUser(): User? {
    return user
  }

  companion object {
    var user: User? = null
  }
}