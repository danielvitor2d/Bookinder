package com.mobile.bookinder.screens.sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.mobile.bookinder.common.User
import com.mobile.bookinder.databinding.ActivitySignUpBinding
import com.mobile.bookinder.screens.dao.UserDAO
import com.mobile.bookinder.screens.home.Home

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

            val userDao = UserDAO()
            val id = userDao.newId()
            val user = User(id, fieldName, fieldEmail, fieldPassword)

            if (userDao.insert(user)){
                val intent = Intent(this, Home::class.java)
                intent.putExtra("user_id", user.user_id)
                startActivity(intent)
                finish()
            }else{
                Snackbar.make(this, it,"Email já existe!", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}