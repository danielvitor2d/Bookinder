package com.mobile.bookinder.common

import java.util.*

class User(
  val user_id: UUID?,
  val email: String,
  val firstname: String,
  val lastname: String?,
  val password: String,
  val photo_id: UUID?,
  val books: MutableList<UUID>?
) {
  constructor(_user_id: UUID?, _firstname:String, _email: String, _password: String) :
    this(_user_id, _email, _firstname, "", _password, null, mutableListOf())
}