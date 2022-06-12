package com.mobile.bookinder.screens.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobile.bookinder.common.Book
import com.mobile.bookinder.common.User
import com.mobile.bookinder.databinding.ActivitySignInBinding
import com.mobile.bookinder.screens.dao.UserDAO
import com.mobile.bookinder.screens.home.Home
import com.mobile.bookinder.screens.sign_up.SignUpActivity
import java.util.*

class SignInActivity : AppCompatActivity() {

  private val users: MutableList<User> = mutableListOf(
    User(UUID.randomUUID(), "danielvitor.p1@gmail.com", "daniel123")
    User(UUID.randomUUID(), "teste", "teste")
  )

  private val books: MutableList<Book> = mutableListOf()

  private lateinit var binding: ActivitySignInBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivitySignInBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setUpListeners()
  }

  private fun setUpListeners() {
    //Texto para SignUp
    binding.signUpText.setOnClickListener{
      val intent = Intent(this, SignUpActivity::class.java)
      startActivity(intent)
    }

    //Botão de Login
    binding.button.setOnClickListener {
      val fieldEmail = binding.editTextEmail.text.toString()
      val fieldPassword = binding.editTextPassword.text.toString()

      val userDao = UserDAO()
      if(userDao.find(fieldEmail, fieldPassword) is User) {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
      }else{
        Toast.makeText(this, "Email e/ou senha inválidos", Toast.LENGTH_SHORT).show()
      }
    }

    //Botão de cadastro
    binding.buttonRegistration.setOnClickListener {
      val intent = Intent(this, SignUpActivity::class.java)
      startActivity(intent)
    }

    //Botão Entrar com Google
    binding.gooleBtn.setOnClickListener {

    }
    //Botão Entrar com Facebook
    binding.faceBtn.setOnClickListener {

    }
  }
}