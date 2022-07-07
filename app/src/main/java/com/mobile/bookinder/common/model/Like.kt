package com.mobile.bookinder.common.model

import java.util.*


class Like(
  var like_id: String,
  var user_id_from: String,
  var user_id_to: String,
  var book_id_to: String,
  var date: Date,
  var linked_match: String?,
) {
  constructor(like_id: String, user_id_from: String, user_id_to: String, book_id_to: String):
    this(like_id, user_id_from, user_id_to, book_id_to, Date(), null)
}