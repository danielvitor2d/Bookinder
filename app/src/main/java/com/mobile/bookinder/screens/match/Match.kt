package com.mobile.bookinder.screens.match

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mobile.bookinder.databinding.ActivityMatchBinding

class Match: AppCompatActivity() {
  private lateinit var binding: ActivityMatchBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMatchBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setUpActionBar()
  }

  private fun setUpActionBar() {
    setSupportActionBar(binding.toolbarMatch)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Match"
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      android.R.id.home -> {
        finish()
        return true
      }
    }
    return super.onContextItemSelected(item)
  }
}