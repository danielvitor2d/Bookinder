package com.mobile.bookinder.common.model

import java.util.*

class Match(
  var match_id: UUID?,
  var book_id_01: UUID,
  var book_id_02: UUID,
  var user_id_01: UUID,
  var user_id_02: UUID,
  var like_id_01: UUID,
  var like_id_02: UUID,
  var feedback_id_01: UUID?,
  var feedback_id_02: UUID?,
  var date: Date
  ) {}