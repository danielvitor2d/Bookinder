package com.mobile.bookinder.common.model

import java.io.Serializable

data class Book(
  var book_id: String? = null,
  var title: String? = null,
  var author: String? = null,
  var gender: String? = null,
  var synopsis: String? = null,
  var owner: String? = null,
  var photos: MutableList<String>? = mutableListOf()
) : Serializable {
  constructor(_book_id: String, _title: String, _author: String, _gender: String, _synopsis: String, _owner: String?) :
    this(_book_id, _title, _author, _gender, _synopsis, _owner, mutableListOf())
}