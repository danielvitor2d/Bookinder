package com.mobile.bookinder.common.models

data class User(
  var id: String,
  var email: String,
  var firstname: String,
  var lastname: String?,
  var photo: String?,
  var books: MutableList<String?>?
) {
  constructor(_email: String, _firstname: String):
    this("", _email, _firstname, "", null, null)
}