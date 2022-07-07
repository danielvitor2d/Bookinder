package com.mobile.bookinder.screens.loading

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mobile.bookinder.common.model.User

import com.mobile.bookinder.databinding.ActivityLoadingBinding
import com.mobile.bookinder.screens.home.HomeActivity
import com.mobile.bookinder.screens.sign_in.SignInActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoadingActivity: AppCompatActivity() {
  private lateinit var binding: ActivityLoadingBinding

  private lateinit var progressBar: ProgressBar

  private val auth = Firebase.auth
  private val db = Firebase.firestore

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoadingBinding.inflate(layoutInflater)
    setContentView(binding.root)

    progressBar = binding.progressBar
    showProgress(true)

    if (auth.currentUser != null) {
      GlobalScope.launch {
        val bundle = Bundle()

        val resultQuery = db.collection("users")
          .whereEqualTo("email", auth.currentUser?.email)
          .get()
          .await()

        if (resultQuery.documents.size > 0 && resultQuery.documents[0].exists()) {
          val user = resultQuery.documents[0].toObject<User>()
          Log.d("User: ", user.toString())
          bundle.putSerializable("user", user)
          updateUI(applicationContext, HomeActivity::class.java, bundle)
        }
      }
    } else {
      updateUI(applicationContext, SignInActivity::class.java, null)
    }

  }

  private fun showProgress(show: Boolean) {
    progressBar.visibility = if (show) View.VISIBLE else View.GONE
  }

  private fun updateUI(context: Context, toClass: Class<*>, bundle: Bundle?) {
    val intent = Intent(context, toClass)
    if (bundle != null) {
      intent.putExtras(bundle)
    }
    startActivity(intent)
    finish()
  }

//  private suspend fun signInAnonymously() {
//    auth.signInAnonymously().await()
//    updateUI(this, SignInActivity::class.java)
//  }
}