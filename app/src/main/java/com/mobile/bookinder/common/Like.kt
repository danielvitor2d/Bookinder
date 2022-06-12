package com.mobile.bookinder.common

import java.util.*

class Like(
  val like_id: UUID,
  val user_id_from: UUID,
  val user_id_to: UUID,
  val book_id_to: UUID,
  val date: Date,
  val linked_match: UUID?,
) {}