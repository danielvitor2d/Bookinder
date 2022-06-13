package com.mobile.bookinder.common.model

import java.util.*

class Feedback(
  var feedback_id: UUID,
  var description: String,
  var stars: Byte,
  var match_id: UUID,
  var user_id_from: UUID,
  var user_id_to: UUID,
  var date: Date
) {}