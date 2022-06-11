package com.mobile.bookinder.screens.feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.mobile.bookinder.databinding.FeedbackBinding

class Feedback : AppCompatActivity() {
  private lateinit var binding: FeedbackBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = FeedbackBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setUpActionBar()
  }

  private fun setUpActionBar() {
    setSupportActionBar(binding.toolbarFeedback)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Feedback"
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