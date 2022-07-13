package com.mobile.bookinder.screens.sign_up

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.ActivitySignUpBinding
import com.mobile.bookinder.screens.sign_in.SignInActivity
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SignUpActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySignUpBinding

  private val userDAO = UserDAO()

  private var profilePhoto: Uri? = null
  private var permissionEnabled: Boolean = false

  private val storage = Firebase.storage
  private val db = Firebase.firestore
  private val auth = Firebase.auth

  private val storageRef = storage.reference
  private val imagesRef = storageRef.child("images")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivitySignUpBinding.inflate(layoutInflater)
    setContentView(binding.root)

    permissionEnabled = getPermission()

    setUpListeners();
  }

  // Checa se existe ou não permissão
  private fun initPermissions() {
    if (!getPermission()) setPermission()
    else permissionEnabled = true
  }

  // Checa se existe permissão
  private fun getPermission(): Boolean =
    (ContextCompat.checkSelfPermission(
      this,
      Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

  // Pede permissão se não tiver
  private fun setPermission() {
    val permissionsList = listOf(
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
    grantResults: IntArray,
  ) {
    when (requestCode) {
      PERMISSION_CODE -> {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
          errorPermission()
        } else {
          permissionEnabled = true
        }
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  private fun setUpListeners() {
    val selectImage =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
          binding.imagePerfil.setImageURI(result.data?.data)
          profilePhoto = result.data?.data
        }
      }

    binding.buttonAddPhoto.setOnClickListener {
      val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
      if (permissionEnabled) {
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
        Toast.makeText(this, "É obrigatório preencher todos os campos", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      if (fieldPassword.length < 8) {
        Toast.makeText(this, "A senha deve conter no mínimo 8 caracteres", Toast.LENGTH_SHORT)
          .show()
      }

      if (profilePhoto != null && profilePhoto!!.path != null && profilePhoto!!.path?.isNotEmpty() == true) {
        val imagePath = "${UUID.randomUUID()}_${profilePhoto!!.path?.let { it1 -> File(it1).name }}"
        val imageRef = imagesRef.child(imagePath)
        val uploadTask = imageRef.putFile(profilePhoto!!)

        uploadTask
          .addOnProgressListener { task ->
            val progress = (100.0 * task.bytesTransferred) / task.totalByteCount
            Log.d(ContentValues.TAG, "Upload is $progress% done")
          }
          .addOnSuccessListener {
            createUser(fieldEmail, fieldPassword, fieldName, "images/${imagePath}", this)
          }
          .addOnFailureListener { e ->
            Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT)
              .show()
            Log.d("Error: ", "${e.message}")
          }
      } else {
        createUser(fieldEmail, fieldPassword, fieldName, null, this)
      }
    }
  }

  private fun createUser(
    email: String,
    password: String,
    firstname: String,
    imageID: String?,
    context: Context,
  ) {
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val user = User(task.result.user?.uid, firstname, email)

          if (imageID != null) {
            user.photo = imageID
          }

          db.collection("users").document(user.user_id.toString())
            .set(user)
            .addOnSuccessListener {
              Toast.makeText(context, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show()

              val intent = Intent(this, SignInActivity::class.java)
              startActivity(intent)
              finish()
            }
            .addOnFailureListener { e ->
              Toast.makeText(context, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
              e.message?.let { Log.d("Erro cadastrar usuario", it) }
            }
        } else {
          Toast.makeText(context, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
        }
      }
  }

  companion object {
    private const val PERMISSION_CODE = 1
  }
}