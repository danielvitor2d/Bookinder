package com.mobile.bookinder.common.model

import java.util.*

class User(
  var user_id: UUID?,
  var email: String,
  var firstname: String,
  var lastname: String?,
  var password: String,
  var photo_id: UUID?,
  var books: MutableList<UUID>?
) {
  constructor(_user_id: UUID?, _firstname:String, _email: String, _password: String) :
    this(_user_id, _email, _firstname, "", _password, null, mutableListOf())
}