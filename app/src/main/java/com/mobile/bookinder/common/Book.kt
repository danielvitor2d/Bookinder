package com.mobile.bookinder.common

import java.util.*

class Book(
  val book_id: UUID?,
  val title: String,
  val author: String?,
  val synopsis: String?,
  val owner: UUID?,
  val photos: MutableList<UUID>?
) {
  constructor(_book_id: UUID?, _title: String, _owner: UUID?) :
    this(_book_id, _title, null, null, _owner, null)
}