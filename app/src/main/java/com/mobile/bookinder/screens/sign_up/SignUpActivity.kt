package com.mobile.bookinder.screens.sign_up

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivitySignUpBinding
import com.mobile.bookinder.common.dao.UserDAO
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