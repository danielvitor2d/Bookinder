package com.mobile.bookinder.common.dao

import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.Match
import java.util.*

class LikeDAO {

  private var matchDAO = MatchDAO()

  fun insert(new_like: Like): Boolean{
    for (like in likeList) {
      if (new_like.like_id == like.like_id)
        return false
    }
    likeList.add(new_like)
    return true
  }

  fun allByUser(user_id: String?): MutableList<Like> {
    return likeList.filter { it.user_id_to == user_id } as MutableList<Like>
  }

  fun deslike(like: Like?){
    if (likeList.contains(like)){
      likeList.remove(like)
    }
  }

  fun findLike(user_id: String, book_id: String): Like?{
    for (like in likeList){
      if (like.user_id_from == user_id && like.book_id_to == book_id){
        return like
      }
    }
    return null
  }

  fun booksILiked(user_id: String?): MutableList<String> { //retorna os livros que curti =D
    val booksUuid: MutableList<String> = mutableListOf()
    for (like in likeList){
      if(like.user_id_from == user_id){
        booksUuid.add(like.book_id_to)
      }
    }
    return booksUuid
  }

  fun all(): MutableList<Like>{
    return likeList
  }

  companion object {
    val likeList: MutableList<Like> = mutableListOf()
  }
}