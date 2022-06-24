package com.mobile.bookinder.common.dao

import com.mobile.bookinder.common.model.Like
import java.util.*

class LikeDAO {

  fun insert(new_like: Like): Boolean{
    for (like in likeList) {
      if (new_like.like_id == like.like_id)
        return false
    }
    likeList.add(new_like)
    return true
  }

  fun allByUser(user_id: UUID?): MutableList<Like> {
    return likeList.filter { it.user_id_to == user_id } as MutableList<Like>
  }

  fun all(): MutableList<Like>{
    return likeList
  }

  companion object {
    val likeList: MutableList<Like> = mutableListOf()
  }
}