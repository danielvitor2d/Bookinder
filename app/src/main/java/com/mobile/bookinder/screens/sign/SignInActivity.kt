package com.mobile.bookinder.screens.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.bookinder.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Texto para SignUp
        binding.signUpText.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //Botão de Login
        binding.button.setOnClickListener {
            //Só colocar o intent para a home
        }

        //Botão Entrar com Google
        binding.gooleBtn.setOnClickListener {

        }

        //Botão Entrar com Facebook
        binding.faceBtn.setOnClickListener {

        }

    }
}