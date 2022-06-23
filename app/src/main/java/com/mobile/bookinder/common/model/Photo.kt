package com.mobile.bookinder.common.model

import java.util.*

class Photo(
  var photo_id: UUID?,
  var path: String
) {
  constructor(_photo_id: UUID?) :
    this(_photo_id, "")

  @JvmName("setPath1")
  fun setPath(new_path: String){
    this.path = new_path
  }
}