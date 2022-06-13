package com.mobile.bookinder.common.model

import java.util.*

class Like(
  var like_id: UUID,
  var user_id_from: UUID,
  var user_id_to: UUID,
  var book_id_to: UUID,
  var date: Date,
  var linked_match: UUID?,
) {}