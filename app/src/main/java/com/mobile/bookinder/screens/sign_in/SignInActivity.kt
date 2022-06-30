package com.mobile.bookinder.screens.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivitySignInBinding
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.screens.home.HomeActivity
import com.mobile.bookinder.screens.sign_up.SignUpActivity
import java.util.*

class SignInActivity : AppCompatActivity() {
  private val auth = Firebase.auth
//  private val storage = Firebase.storage
//  private val db = Firebase.firestore

  private lateinit var binding: ActivitySignInBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivitySignInBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setUpListeners()
  }

  private fun setUpListeners() {
    binding.signInButton.setOnClickListener {
      val fieldEmail = binding.editTextUserEmail.text.toString()
      val fieldPassword = binding.editTextUserPassword.text.toString()

      if (fieldEmail.isEmpty() || fieldPassword.isEmpty()) {
        Toast.makeText(this, "Os campos não podem estar vazios!", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      auth.signInWithEmailAndPassword(fieldEmail, fieldPassword)
        .addOnCompleteListener(this) { task ->
          if (task.isSuccessful) {
            Toast.makeText(this, "Usuário logado com sucesso!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
          } else {
            Toast.makeText(this, "E-mail e/ou senha inválidos", Toast.LENGTH_SHORT).show()
          }
        }

//      // Create a new user with a first and last name
//      val user = hashMapOf(
//        "first" to "Alan",
//        "middle" to "Mathison",
//        "last" to "Turing",
//        "born" to 1912
//      )
//
//      // Add a new document with a generated ID
//      db.collection("users")
//        .add(user)
//        .addOnSuccessListener { documentReference ->
//          Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//          Toast.makeText(this, "Documento adicionado com ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
//        }
//        .addOnFailureListener { e ->
//          Log.w(TAG, "Error adding document", e)
//          Toast.makeText(this, "Erro ao adicionar documento: $e!", Toast.LENGTH_LONG).show()
//        }
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