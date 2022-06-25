package com.mobile.bookinder.common.model

import java.util.*

class Book(
  var book_id: UUID,
  var title: String,
  var author: String,
  var gender: String,
  var synopsis: String,
  var owner: UUID?,
  var photos: MutableList<UUID>
) {
  constructor(_book_id: UUID, _title: String, _author: String, _gender: String, _synopsis: String, _owner: UUID?) :
    this(_book_id, _title, _author, _gender, _synopsis, _owner, mutableListOf())
}