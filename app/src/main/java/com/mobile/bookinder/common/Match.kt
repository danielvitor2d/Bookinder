package com.mobile.bookinder.common

import java.util.*

class Match(
  val match_id: UUID?,
  val book_id_01: UUID,
  val book_id_02: UUID,
  val user_id_01: UUID,
  val user_id_02: UUID,
  val like_id_01: UUID,
  val like_id_02: UUID,
  val feedback_id_01: UUID?,
  val feedback_id_02: UUID?,
  val date: Date
  ) {}