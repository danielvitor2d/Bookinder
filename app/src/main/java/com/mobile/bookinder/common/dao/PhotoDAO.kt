package com.mobile.bookinder.common.dao

import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Photo
import java.util.*

class PhotoDAO {
  fun insert(new_photo: Photo, book_id: UUID): Boolean{
    val bookDAO = BookDAO()
    for (photo in photoList) {
      if (photo.photo_id == new_photo.photo_id)
        return false
    }

    bookDAO.addPhoto(book_id, new_photo.photo_id)
    photoList.add(new_photo)

    return true
  }

  fun findById(id: UUID) : Photo?{
    for (photo in photoList) {
      if (photo.photo_id == id)
        return photo
    }
    return null
  }

  companion object {
    val photoList: MutableList<Photo> = mutableListOf()
  }
}