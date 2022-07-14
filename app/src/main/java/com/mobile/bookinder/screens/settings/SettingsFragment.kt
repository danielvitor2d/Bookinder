package com.mobile.bookinder.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobile.bookinder.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {

  private var _binding: FragmentSettingsBinding? = null
  private val binding get() = _binding!!

  private val auth = Firebase.auth
  private val db = Firebase.firestore

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSettingsBinding.inflate(inflater, container, false)

    binding.updatePassword.setOnClickListener {
      val senha = binding.editTextPassword.text.toString()
      val confirmarSenha = binding.editTextConfirmPassword.text.toString()

      if (senha.isEmpty() || confirmarSenha.isEmpty()) {
        Toast.makeText(binding.root.context, "Senha não pode ser vazia!", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      if (!senha.equals(confirmarSenha)) {
        Toast.makeText(binding.root.context, "Senhas diferentes!", Toast.LENGTH_SHORT).show()
      } else {
//        auth.
      }
    }

    return binding.root
  }

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}