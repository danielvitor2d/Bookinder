package com.mobile.bookinder.screens.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.databinding.FragmentProfileBinding
import com.mobile.bookinder.screens.feedback.Feedback
import com.mobile.bookinder.screens.home.HomeActivity

class ProfileFragment: Fragment() {
  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private lateinit var profileFirstNameUser: EditText
  private lateinit var profileLastNameUser: EditText
  private lateinit var profileEmailUser: EditText
  private lateinit var profileSaveButton: AppCompatButton
  private lateinit var goToFeedbackButton: AppCompatButton

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentProfileBinding.inflate(inflater, container, false)

    setUpViews()
    setUpLoggedUser()
    setUpListeners(binding.root.context)

    return binding.root
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }

  private fun setUpViews() {
    profileFirstNameUser = binding.profileFirstNameUser
    profileLastNameUser = binding.profileLastNameUser
    profileEmailUser = binding.profileEmailUser
    profileSaveButton = binding.profileSaveButton
    goToFeedbackButton = binding.goToFeedbackButton
  }

  private fun setUpLoggedUser() {
    val user = LoggedUser.user
    profileFirstNameUser.setText(user?.firstname)
    profileLastNameUser.setText(user?.lastname)
    profileEmailUser.setText(user?.email)
  }

  private fun setUpListeners(context: Context) {
    profileSaveButton.setOnClickListener {
      val firstname = profileFirstNameUser.text.toString()
      val lastname = profileLastNameUser.text.toString()
      val email = profileEmailUser.text.toString()
      if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()) {
        Toast.makeText(context, "Preencha os campos", Toast.LENGTH_LONG).show()
        return@setOnClickListener
      }

      val user = LoggedUser.user
      if (user != null) {
        user.firstname = firstname
        user.lastname = lastname
        user.email = email

        val userDAO = UserDAO()
        userDAO.setUser(user.user_id, user)

        Toast.makeText(context, "Dados alterados com sucesso!", Toast.LENGTH_LONG).show()

        updateHeader()
      }
    }
    goToFeedbackButton.setOnClickListener {
      val intent = Intent(context, Feedback::class.java)
      startActivity(intent)
    }
  }

  private fun updateHeader() {
    val user = LoggedUser.user

    val headerView = activity?.findViewById<NavigationView>(R.id.navigation_view)?.getHeaderView(0)

    val textViewUserName = headerView?.findViewById<TextView>(R.id.textViewUserName)
    "${user?.firstname} ${user?.lastname}".also {
      textViewUserName?.text = it
    }

    val textViewUserEmail = headerView?.findViewById<TextView>(R.id.textViewUserEmail)
    "${user?.email}".also {
      textViewUserEmail?.text = it
    }
  }
}