package com.mobile.bookinder.common

import java.util.*

class Book(
  val book_id: Int,
  val title: String,
  val author: String,
  val synopsis: String,
  val owner: Int,
  val photos: MutableList<UUID>
) {
  constructor(id: Int, _title: String, _author: String, _synopsis: String, _owner: Int) :
    this(id, _title, _author, _synopsis, _owner, mutableListOf())
}