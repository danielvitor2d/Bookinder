package com.mobile.bookinder.common

import java.util.*

class Feedback(
  val feedback_id: UUID,
  val description: String,
  val stars: Byte,
  val match_id: UUID,
  val user_id_from: UUID,
  val user_id_to: UUID,
  val date: Date
) {}