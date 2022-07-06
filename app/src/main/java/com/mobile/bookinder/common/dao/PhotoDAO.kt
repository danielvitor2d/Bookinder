package com.mobile.bookinder.common.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.common.model.User
import java.io.File
import java.io.FileOutputStream
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

  fun insert(new_photo: Photo, user: User): Boolean{
    val userDAO = UserDAO()
    if (userDAO.find(user.user_id)){
      for (photo in photoList) {
        if (photo.photo_id == new_photo.photo_id)
          return false
      }
      userDAO.insertPhoto(user, new_photo)
      photoList.add(new_photo)
      return true
    }
    return false
  }

  fun remove(photo: Photo, user: User){
    user.photo_id = null
    photoList.remove(photo)
  }

  fun removePhotoBook(photo: UUID){
    for (p in photoList){
      if (p.photo_id == photo){
        photoList.remove(p)
      }
    }
  }

  fun findById(id: UUID?) : Photo?{
    for (photo in photoList) {
      if (photo.photo_id == id)
        return photo
    }
    return null
  }

  fun getContactBitmapFromURI(context: Context, uri: Uri): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
  }

  fun saveBitmapIntoSDCardImage(context: Context, bitmap: Bitmap, fname: String): File {
    val myDir = context.cacheDir
    myDir.mkdirs()

    val file = File(myDir, fname)

    val fileOutputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
    fileOutputStream.flush()
    fileOutputStream.close()

    return file
  }

  companion object {
    val photoList: MutableList<Photo> = mutableListOf()
  }
}