package com.mobile.bookinder.screens.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.mobile.bookinder.R
import com.mobile.bookinder.screens.home.Home

class SignInActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_in)
    setUpListeners()
  }

  private fun setUpListeners() {
    val signUpText = findViewById<TextView>(R.id.signUpText)
    val button = findViewById<Button>(R.id.button)
    val gooleBtn = findViewById<ImageView>(R.id.goole_btn)
    val faceBtn = findViewById<ImageView>(R.id.face_btn)

    //Texto para SignUp
    signUpText.setOnClickListener{
      val intent = Intent(this, SignUpActivity::class.java)
      startActivity(intent)
    }
    //Botão de Login
    button.setOnClickListener {
      //Só colocar o intent para a home
      val intent = Intent(this, Home::class.java)
      startActivity(intent)
    }
    //Botão Entrar com Google
    gooleBtn.setOnClickListener {
    }
    //Botão Entrar com Facebook
    faceBtn.setOnClickListener {
    }
  }
}