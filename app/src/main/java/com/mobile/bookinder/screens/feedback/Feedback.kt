package com.mobile.bookinder.screens.feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatButton
import com.mobile.bookinder.databinding.ActivityFeedbackBinding

class Feedback : AppCompatActivity() {
  private lateinit var binding: ActivityFeedbackBinding
  private lateinit var cancelButton: AppCompatButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityFeedbackBinding.inflate(layoutInflater)
    setContentView(binding.root)

    cancelButton = binding.cancelButton

    setUpActionBar()
    setUpListeners()
  }

  private fun setUpListeners() {
    cancelButton.setOnClickListener {
      finish()
    }
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