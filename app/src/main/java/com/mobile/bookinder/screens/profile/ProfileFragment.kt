package com.mobile.bookinder.screens.profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.model.User
import com.mobile.bookinder.databinding.FragmentProfileBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class ProfileFragment : Fragment() {
  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private lateinit var profileFirstNameUser: EditText
  private lateinit var profileLastNameUser: EditText
  private lateinit var profileEmailUser: EditText
  private lateinit var profileSaveButton: AppCompatButton
  private lateinit var imagePerfil: ImageView
  private var profilePhoto: Uri? = null
  private var user: User? = null

  private val auth = Firebase.auth
  private val db = Firebase.firestore
  private val storage = Firebase.storage

  private val storageRef = storage.reference
  private val imagesRef = storageRef.child("images")

  // Variável de checagem de permissão
  private var permissionEnabled: Boolean = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentProfileBinding.inflate(inflater, container, false)

    permissionEnabled = getPermission()

    setUpViews()
    setUpLoggedUser()
    setUpListeners(binding.root.context)

    return binding.root
  }

  // Checa se existe ou não permissão
  private fun initPermissions() {
    if (!getPermission()) setPermission()
    else permissionEnabled = true
  }

  // Checa se existe permissão
  private fun getPermission(): Boolean =
    (ContextCompat.checkSelfPermission(
      this.context as Activity,
      Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

  // Pede permissão se não tiver
  private fun setPermission() {
    val permissionsList = listOf<String>(
      Manifest.permission.READ_EXTERNAL_STORAGE,
    )
    ActivityCompat.requestPermissions(this.context as Activity, permissionsList.toTypedArray(),
      PERMISSION_CODE)
  }

  // Envia mensagem por não ter permissão
  private fun errorPermission() {
    Toast.makeText(context, "Não tem permissão para ler arquivos", Toast.LENGTH_SHORT).show()
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

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setUpViews() {
    profileFirstNameUser = binding.profileFirstNameUser
    profileLastNameUser = binding.profileLastNameUser
    profileEmailUser = binding.profileEmailUser
    profileSaveButton = binding.profileSaveButton
    imagePerfil = binding.profileAvatarUser
  }

  private fun setUpLoggedUser() {
    db.collection("users")
      .whereEqualTo("email", auth.currentUser?.email)
      .get()
      .addOnSuccessListener { query ->
        user = query.documents[0].toObject<User>()

        profileFirstNameUser.setText(user?.firstname)
        profileLastNameUser.setText(user?.lastname)
        profileEmailUser.setText(user?.email)

        val imageUrl = user?.photo
        if (imageUrl is String) {
          val storageRef = storage.reference
          val imageRef = storageRef.child(imageUrl)

          imageRef.downloadUrl.addOnSuccessListener {
            Glide.with(this)
              .load(it.toString())
              .centerCrop()
              .into(imagePerfil)
          }
        }
      }
  }

  private fun setUpListeners(context: Context) {
    val selectImage =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
          imagePerfil.setImageURI(result.data?.data)
          profilePhoto = result.data?.data
        }
      }

    binding.buttonAlterPhoto.setOnClickListener {
      val intent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.INTERNAL_CONTENT_URI)
      if (permissionEnabled) {
        selectImage.launch(intent)
      } else {
        initPermissions()
      }
    }

    profileSaveButton.setOnClickListener {
      val firstname = profileFirstNameUser.text.toString()
      val lastname = profileLastNameUser.text.toString()
      val email = profileEmailUser.text.toString()

      if (firstname.isEmpty() || email.isEmpty()) {
        Toast.makeText(context, "Preencha os campos", Toast.LENGTH_LONG).show()
        return@setOnClickListener
      }

      val updates = hashMapOf<String, Any>(
        "firstname" to firstname,
        "lastname" to lastname
      )

      var imagePath = ""
      if (profilePhoto != null && profilePhoto!!.path != null && profilePhoto!!.path?.isNotEmpty() == true) {
        imagePath = "${UUID.randomUUID()}_${profilePhoto!!.path?.let { path -> File(path).name }}"
        updates["photo"] = "images/$imagePath"
        val imageRef = imagesRef.child(imagePath)
        imageRef.putFile(profilePhoto!!)
        val lastPhotoRef = user?.photo?.let { lastPhoto -> storageRef.child(lastPhoto) }
        lastPhotoRef?.delete()
      }

      db.collection("users")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener {
          for(document in it.documents)
            document.reference.update(updates)

          Toast.makeText(context, "Usuário atualizado com sucesso", Toast.LENGTH_LONG).show()

          val headerView =
            activity?.findViewById<NavigationView>(R.id.navigation_view)?.getHeaderView(0)
          val textViewUserName = headerView?.findViewById<TextView>(R.id.textViewUserName)
          "$firstname $lastname".also { textViewUserName?.text = it }

          val photoView = headerView?.findViewById(R.id.imageView3) as ImageView

          if (imagePath.isNotEmpty()) {
            photoView.setImageURI(profilePhoto)
          }
        }.addOnFailureListener{
          Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
  }

  companion object {
    private const val PERMISSION_CODE = 1
  }
}