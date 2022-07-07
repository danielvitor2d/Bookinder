package com.mobile.bookinder.common.model

import java.util.*

class Match(
  var match_id: String?,
  var book_id_01: String,
  var book_id_02: String,
  var user_id_01: String,
  var user_id_02: String,
  var like_id_01: String,
  var like_id_02: String,
  var feedback_id_01: String?,
  var feedback_id_02: String?,
  var date: Date
  ) {

}