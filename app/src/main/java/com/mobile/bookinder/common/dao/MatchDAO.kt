package com.mobile.bookinder.common.dao

import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.Match
import java.util.*

class MatchDAO {
  fun insert(new_match: Match): Boolean{
    for (match in matchList) {
      if (match.match_id == new_match.match_id)
        return false
    }
    matchList.add(new_match)
    return true
  }

  fun likeMacth(like: Like){
    val userFrom = like.user_id_from
    val userTo = like.user_id_to

    for (i in LikeDAO.likeList){
      if(i.user_id_from == userTo && i.book_id_to == userFrom){
        val match = Match(UUID.randomUUID(), like.book_id_to, i.like_id, like.user_id_from, i.user_id_from, like.like_id, i.like_id, null, null, Date())
        matchList.add(match)
      }
    }
  }

  fun deslikeMatch(like: Like?){
    if(like != null){
      val userFrom = like.user_id_from
      val userTo = like.user_id_to
      val book = like.book_id_to

      for (match in matchList){
        if (match.user_id_01 == userFrom && match.user_id_02 == userTo &&
          (match.book_id_01 == book || match.book_id_02 == book) && match.date == like.date){
          matchList.remove(match)
        }
      }
    }
  }

  companion object {
    val matchList: MutableList<Match> = mutableListOf()
  }
}