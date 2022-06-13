package com.mobile.bookinder.common

import android.os.Parcelable
import java.util.*

class User(
  var user_id: Int,
  var email: String,
  var firstname: String,
  var lastname: String?,
  var password: String,
  var photo_id: UUID?,
  var books: MutableList<Int>?
) {
  constructor(id: Int, _email: String, _firstname: String, _password: String) :
    this(id, _email, _firstname, "", _password, null, mutableListOf())

  constructor(_email: String, _password: String) :
    this(-1, _email, "", "", _password, null, mutableListOf())
}