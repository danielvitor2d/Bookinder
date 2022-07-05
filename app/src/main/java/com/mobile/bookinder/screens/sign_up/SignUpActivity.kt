package com.mobile.bookinder.screens.sign_up

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
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
import com.mobile.bookinder.common.models.User
import com.mobile.bookinder.databinding.ActivitySignUpBinding
import com.mobile.bookinder.util.URIPathHelper
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SignUpActivity : AppCompatActivity() {
  private val auth = Firebase.auth
  private val storage = Firebase.storage
  private val db = Firebase.firestore

  private lateinit var binding: ActivitySignUpBinding
  private var profilePhoto: Uri? = null
  private var uriPath = URIPathHelper()

  // Variável de chacagem de permissão
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
        Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show()
        return@setOnClickListener
      }

      val storageRef = storage.reference
      val imagesRef = storageRef.child("images")

      if (profilePhoto != null) {
        val file = profilePhoto as Uri
        val imagePath = "${UUID.randomUUID()}_${File(file.path).name}"
        val imageRef = imagesRef.child(imagePath)
        val uploadTask = imageRef.putFile(file)
        uploadTask
          .addOnProgressListener { uploadTask ->
            val progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
            Log.d(TAG, "Upload is $progress% done")
          }
          .addOnSuccessListener {
            createUser(fieldEmail, fieldPassword, fieldName, "images/${imagePath}")
          }
          .addOnFailureListener { e ->
            Toast.makeText(this, "Erro ao cadastrar imagem (Storage): $e", Toast.LENGTH_SHORT).show()
          }
      } else {
        createUser(fieldEmail, fieldPassword, fieldName, null)
      }
    }
  }

  private fun createUser(email: String, password: String, firstname: String, imageID: String?) {
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          val user = User(email, firstname)

          if (imageID != null) {
            user.photo = imageID
          }

          db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
              // documentReference
              Toast.makeText(this, "Usuário cadastrado com sucesso ${documentReference.id}!", Toast.LENGTH_SHORT).show()
              finish()
            }
            .addOnFailureListener { e ->
              Toast.makeText(this, "Erro ao cadastrar usuário: $e", Toast.LENGTH_SHORT).show()
            }
        } else {
          Toast.makeText(this, "Erro ao cadastrar novo usuário", Toast.LENGTH_LONG).show()
        }
      }
  }

  companion object {
    private const val PERMISSION_CODE = 1

    fun getContactBitmapFromURI(context: Context, uri: Uri): Bitmap {
      val inputStream = context.contentResolver.openInputStream(uri)
      return BitmapFactory.decodeStream(inputStream)
    }

    fun saveBitmapIntoSDCardImage(context: Context, bitmap: Bitmap, fname: String): File {
      val myDir = context.cacheDir
      myDir.mkdirs()

      val file = File(myDir, fname)

      val fileOutputStream = FileOutputStream(file)
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
      fileOutputStream.flush()
      fileOutputStream.close()

      return file
    }
  }
}