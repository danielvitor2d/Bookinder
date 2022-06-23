package com.mobile.bookinder.screens.sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.LikeDAO
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivitySignInBinding
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.screens.home.HomeActivity
import com.mobile.bookinder.screens.sign_up.SignUpActivity
import java.util.UUID.randomUUID

class SignInActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySignInBinding

  private val userDAO = UserDAO()
  private val likeDAO = LikeDAO()
  private val bookDAO = BookDAO()
  private val loggedUser = LoggedUser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivitySignInBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Dados mockados
    val uuid01 = randomUUID()
    val uuid02 = randomUUID()
    val uuid03 = randomUUID()
    userDAO.insert(User(uuid01, "Daniel", "daniel", "daniel"))
    userDAO.insert(User(uuid02, "test", "test", "test"))
    userDAO.insert(User(uuid03, "p", "p", "p"))

    val uuid04 = randomUUID()
    val uuid05 = randomUUID()
    bookDAO.insert(
      Book(
        uuid04,
        "A garota na árvore",
        "Joao Verde",
        "Era uma vez...",
        uuid01
      ),
      userDAO.getById(uuid01)
    )
    bookDAO.insert(
      Book(
        uuid05,
        "João e o pé de feijão",
        "Silvio Santos",
        "Num certo dia...",
        uuid01
      ),
      userDAO.getById(uuid01)
    )

    likeDAO.insert(Like(randomUUID(), uuid02, uuid01, uuid04))

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
