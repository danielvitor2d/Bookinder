package com.mobile.bookinder.common.model

import java.io.Serializable
import java.util.*

data class User(
  var user_id: String? = null,
  var email: String? = null,
  var firstname: String? = null,
  var lastname: String? = null,
  var photo: String? = null,
  var books: MutableList<String>? = mutableListOf()
) : Serializable {
  constructor(uuid: String, _firstname:String, _email: String) :
    this(uuid, _email, _firstname, "", "", mutableListOf())
}