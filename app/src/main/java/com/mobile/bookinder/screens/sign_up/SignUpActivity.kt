package com.mobile.bookinder.screens.sign_up

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.databinding.ActivitySignUpBinding
import java.io.File
import java.io.FileOutputStream

class SignUpActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySignUpBinding
  private var profilePhoto: Uri? = null
  private val userDAO = UserDAO()

  // Variável de checagem de permissão
  private var check = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivitySignUpBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Inicia pedido de permissão
    // initPermissions()

    setUpListeners();
  }

  // Checa se existe ou não permissão
  private fun initPermissions(){
    if(!getPermission()) setPermission()
    else check = true
  }

  // Checa se existe permissão
  private fun getPermission(): Boolean =
    (ContextCompat.checkSelfPermission(
      this,
      Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

  // Pede permissão se não tiver
  private fun setPermission(){
    val permissionsList = listOf<String>(
      Manifest.permission.READ_EXTERNAL_STORAGE,
    )
    ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), PERMISSION_CODE)
  }

  // Envia mensagem por não ter permissão
  private fun errorPermission() {
    Toast.makeText(this, "Não tem permissão para ler arquivos", Toast.LENGTH_SHORT).show()
  }

  // Recebe resultado do pedido de permissão
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when(requestCode){
      PERMISSION_CODE -> {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
          errorPermission()
        } else {
          check = true
        }
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  private fun setUpListeners() {
    val selectImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
      if (result.resultCode == Activity.RESULT_OK){
        binding.imagePerfil.setImageURI(result.data?.data)
        profilePhoto = result.data?.data
      }
    }

    binding.buttonAddPhoto.setOnClickListener {
      val intent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.INTERNAL_CONTENT_URI)
      if (check) {
        selectImage.launch(intent)
      } else {
        initPermissions()
      }
    }

    binding.register.setOnClickListener {
      val fieldName = binding.editTextName.text.toString()
      val fieldEmail = binding.editTextEmail.text.toString()
      val fieldPassword = binding.editTextPassword.text.toString()

      if (fieldName.isEmpty() || fieldEmail.isEmpty() || fieldPassword.isEmpty()) {
        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      userDAO.insert(fieldName, fieldEmail, fieldPassword, profilePhoto, binding.root.context)
//      val user = userDAO.findUser("email", fieldEmail)
//      if(user != null){
//        finish()
//      }

    }
  }



  companion object {
    private const val PERMISSION_CODE = 1

  }
}