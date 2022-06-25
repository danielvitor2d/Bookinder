package com.mobile.bookinder.screens.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivitySignInBinding
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.screens.home.HomeActivity
import com.mobile.bookinder.screens.sign_up.SignUpActivity
import java.util.*

class SignInActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySignInBinding

  private val userDAO = UserDAO()
  private val loggedUser = LoggedUser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivitySignInBinding.inflate(layoutInflater)
    setContentView(binding.root)

    userDAO.insert(User(UUID.randomUUID(), "Daniel", "daniel", "daniel"))
    userDAO.insert(User(UUID.randomUUID(), "", "", ""))

    setUpListeners()
  }

  private fun setUpListeners() {
    binding.signInButton.setOnClickListener {
      val fieldEmail = binding.editTextUserEmail.text.toString()
      val fieldPassword = binding.editTextUserPassword.text.toString()

      val user = userDAO.find(fieldEmail, fieldPassword)

      if (user != null) {
        loggedUser.login(user)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
      } else {
        Toast.makeText(this, "Email e/ou senha inválidos", Toast.LENGTH_SHORT).show()
      }
    }

    binding.signUpText.setOnClickListener {
      val intent = Intent(this, SignUpActivity::class.java)
      startActivity(intent)
    }

    binding.gooleBtn.setOnClickListener {

    }
    binding.faceBtn.setOnClickListener {

    }
  }
}