package com.mobile.bookinder.common.models

data class Book(
  var title: String,
  var author: String,
  var gender: String,
  var synopsis: String,
  var owner: String?,
  var photos: MutableList<String>
)