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
  constructor(_email: String, _password: String) :
    this(null, _email, "", "", _password, null, mutableListOf())
}