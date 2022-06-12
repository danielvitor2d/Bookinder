package com.mobile.bookinder.screens.sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.mobile.bookinder.common.User
import com.mobile.bookinder.databinding.ActivitySignUpBinding
import com.mobile.bookinder.screens.dao.UserDAO
import com.mobile.bookinder.screens.sign_in.SignInActivity
import java.util.*

class SignUpActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySignUpBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySignUpBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setUpListeners();
  }

  private fun setUpListeners() {
    binding.register.setOnClickListener {
      val fieldName = binding.editTextName.text.toString()
      val fieldEmail = binding.editTextEmail.text.toString()
      val fieldPassword = binding.editTextPassword.text.toString()

      val user = User(UUID.randomUUID(), fieldName, fieldEmail, fieldPassword)
      val userDao = UserDAO()

      if (userDao.insert(user)) {
        Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()
        finish()
      } else {
        Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_LONG).show()
      }
    }
  }
}