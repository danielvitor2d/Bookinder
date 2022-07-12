package com.mobile.bookinder.screens.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivitySignInBinding
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.screens.home.HomeActivity
import com.mobile.bookinder.screens.sign_up.SignUpActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class SignInActivity : AppCompatActivity() {
  private val auth = Firebase.auth
  private val db = Firebase.firestore

  private lateinit var binding: ActivitySignInBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (auth.currentUser != null) {
      db.collection("users").whereEqualTo("email", auth.currentUser?.email)
        .get()
        .addOnSuccessListener { result ->
          val user = result.documents[0].toObject<User>()

          val bundle = Bundle()
          bundle.putSerializable("user", user)

          val intent = Intent(this, HomeActivity::class.java)
          intent.putExtras(bundle)

          startActivity(intent)
          finish()
        }
    }

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
            GlobalScope.launch {
              val bundle = Bundle()

              val resultQuery = db.collection("users")
                .whereEqualTo("email", auth.currentUser?.email)
                .get()
                .await()

              Log.i("user", resultQuery.documents[0].toString())
              if (resultQuery.documents.size > 0 && resultQuery.documents[0].exists()) {
                val user = resultQuery.documents[0].toObject<User>()
                Log.d("User: ", user.toString())

                bundle.putSerializable("user", user)

                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.putExtras(bundle)

                startActivity(intent)
                finish()
              }
            }

          } else {
            Toast.makeText(this, "E-mail e/ou senha inválidos", Toast.LENGTH_SHORT).show()
          }
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