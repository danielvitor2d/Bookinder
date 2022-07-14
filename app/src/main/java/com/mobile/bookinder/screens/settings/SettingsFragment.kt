package com.mobile.bookinder.screens.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.databinding.FragmentSettingsBinding
import com.mobile.bookinder.screens.maps.MapsActivity

class SettingsFragment: Fragment() {

  private var _binding: FragmentSettingsBinding? = null
  private val binding get() = _binding!!
  private val loggedUser = LoggedUser()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSettingsBinding.inflate(inflater, container, false)

    val user = loggedUser.getUser()

    binding.updatePassword.setOnClickListener {
      val senha = binding.editTextPassword.text.toString()
      val confirmarSenha = binding.editTextConfirmPassword.text.toString()

      if (senha.equals("") || confirmarSenha.equals("")) {
        Toast.makeText(binding.root.context, "Senha não pode ser vazia!", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      if (!senha.equals(confirmarSenha)) {
        Toast.makeText(binding.root.context, "Senhas diferentes!", Toast.LENGTH_SHORT).show()
      } else {
        if (user != null) {
//          user.password = senha

          val userDAO = UserDAO()
          userDAO.setUser(user)

          Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show()
        }
      }
    }

    return binding.root
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}