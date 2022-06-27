package com.mobile.bookinder.screens.sign_up

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.databinding.ActivitySignUpBinding
import com.mobile.bookinder.util.URIPathHelper
import java.util.*

class SignUpActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySignUpBinding
  private var photoPerfil: Uri? = null
  private var uriPath = URIPathHelper()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySignUpBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setUpListeners();
  }

  private fun setUpListeners() {
    val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
      photoPerfil = it
      val myBitmap = BitmapFactory.decodeFile(uriPath.getPath(this, photoPerfil as Uri))
      binding.imagePerfil.setImageBitmap(myBitmap)
    }

    binding.buttonAddPhoto.setOnClickListener {
      selectImage.launch("image/*")
    }

    binding.register.setOnClickListener {

      val fieldName = binding.editTextName.text.toString()
      val fieldEmail = binding.editTextEmail.text.toString()
      val fieldPassword = binding.editTextPassword.text.toString()

      val user = User(UUID.randomUUID(), fieldName, fieldEmail, fieldPassword)
      val check = conditionsInsert(user)
      if (check){
        val userDao = UserDAO()
        val photoDAO = PhotoDAO()
        val booleanInsert = userDao.insert(user)

        if(booleanInsert){
          if(photoPerfil is Uri){
            photoDAO.insert(Photo(UUID.randomUUID(), uriPath.getPath(this, photoPerfil as Uri).toString()), user)
          }
          Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()
          finish()
        } else {
          Toast.makeText(this, "Email já existe no sistema", Toast.LENGTH_LONG).show()
        }
      }else{
        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
      }
    }
  }

  fun conditionsInsert(user: User): Boolean{
    if(user.firstname == "" || user.email == "" || user.password == ""){
      return false
    }
    return true
  }
}