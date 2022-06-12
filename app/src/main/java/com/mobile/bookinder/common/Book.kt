package com.mobile.bookinder.common

import java.util.*

class Book(
  val book_id: UUID?,
  val title: String,
  val author: String,
  val synopsis: String,
  val owner: UUID,
  val photos: MutableList<UUID>
) {}