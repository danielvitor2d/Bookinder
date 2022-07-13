package com.mobile.bookinder.common.model

import java.io.Serializable
import java.util.*


data class Like(
  var like_id: String? = null,
  var user_id_from: String? = null,
  var user_id_to: String? = null,
  var book_id_to: String? = null,
  var date: Date? = null,
  var linked_match: String? = null,
) : Serializable {
  constructor(like_id: String, user_id_from: String, user_id_to: String, book_id_to: String):
    this(like_id, user_id_from, user_id_to, book_id_to, Date(), null)
}