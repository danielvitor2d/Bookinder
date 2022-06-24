package com.mobile.bookinder.common.model

import java.text.SimpleDateFormat
import java.util.*

class Util {
  companion object {
    fun getFormattedDate(date: Date): String {
      val format = "dd/MM/yyyy"
      val formatObject = SimpleDateFormat(format)
      return formatObject.format(date)
    }
    fun getFormattedHour(date: Date): String {
      val format = "hh:mm"
      val formatObject = SimpleDateFormat(format)
      return formatObject.format(date)
    }
  }
}