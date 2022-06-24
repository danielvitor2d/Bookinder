package com.mobile.bookinder.common.model

import android.os.Build
import java.util.*


class Like(
  var like_id: UUID,
  var user_id_from: UUID,
  var user_id_to: UUID,
  var book_id_to: UUID,
  var date: Date,
  var linked_match: UUID?,
) {
  constructor(like_id: UUID, user_id_from: UUID, user_id_to: UUID, book_id_to: UUID):
    this(like_id, user_id_from, user_id_to, book_id_to, Date(), null)
}