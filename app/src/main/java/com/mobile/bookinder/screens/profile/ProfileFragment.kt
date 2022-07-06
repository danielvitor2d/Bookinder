package com.mobile.bookinder.screens.profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.PhotoDAO
import com.mobile.bookinder.common.dao.UserDAO
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.common.model.Photo
import com.mobile.bookinder.databinding.FragmentProfileBinding
import com.mobile.bookinder.screens.other_profile.OtherProfileActivity
import com.mobile.bookinder.screens.sign_up.SignUpActivity
import com.mobile.bookinder.util.URIPathHelper
import java.io.File
import java.util.*

class ProfileFragment: Fragment() {
  private var _binding: FragmentProfileBinding? = null
  private val binding get() = _binding!!

  private lateinit var profileFirstNameUser: EditText
  private lateinit var profileLastNameUser: EditText
  private lateinit var profileEmailUser: EditText
  private lateinit var profileSaveButton: AppCompatButton
  private lateinit var imagePerfil: ImageView
  private var photoPerfil: Uri? = null
  private var uriPath = URIPathHelper()
  private val photoDAO = PhotoDAO()
  private val userDAO = UserDAO()
  private val loggedUser = LoggedUser()

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
    imagePerfil = binding.profileAvatarUser
  }

  private fun setUpLoggedUser() {
    val user = LoggedUser.user
    profileFirstNameUser.setText(user?.firstname)
    profileLastNameUser.setText(user?.lastname)
    profileEmailUser.setText(user?.email)
  }

  private fun setUpListeners(context: Context) {
    var user = loggedUser.getUser()

    val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
      photoPerfil = it
      imagePerfil.setImageURI(photoPerfil)
    }

    binding.buttonAlterPhoto.setOnClickListener {
      selectImage.launch("image/*")
    }

    profileSaveButton.setOnClickListener {
      val firstname = profileFirstNameUser.text.toString()
      val lastname = profileLastNameUser.text.toString()
      val email = profileEmailUser.text.toString()

      if (firstname.isEmpty() || email.isEmpty()) {
        Toast.makeText(context, "Preencha os campos", Toast.LENGTH_LONG).show()
        return@setOnClickListener
      }

      if (user != null) {
        user.firstname = firstname
        user.lastname = lastname
        user.email = email

        val photo = photoDAO.findById(user.photo_id)
        if (photoPerfil != null) {
          if (photo != null) {
//            Toast.makeText(context, "O usuário já tinha uma foto", Toast.LENGTH_LONG).show()
            photoDAO.remove(photo, user)
          }
//          Toast.makeText(context, "Agora ele tem uma nova", Toast.LENGTH_LONG).show()
          val bitmap = photoDAO.getContactBitmapFromURI(it.context, photoPerfil!!)

          val file =
            photoDAO.saveBitmapIntoSDCardImage(it.context, bitmap, "${UUID.randomUUID()}.jpg")

          photoDAO.insert(Photo(UUID.randomUUID(), file.path), user)
        }

        val userDAO = UserDAO()
        userDAO.setUser(user)

        Toast.makeText(context, "Dados alterados com sucesso!", Toast.LENGTH_LONG).show()

        updateHeader()
      }
    }
//    goToFeedbackButton.setOnClickListener {
////      val intent = Intent(context, Feedback::class.java)
////      startActivity(intent)
//      var bundle = Bundle()
//      bundle.putString("user_id", userDAO.find("teste", "teste")?.user_id.toString())
//      val intent = Intent(context, OtherProfileActivity::class.java)
//      intent.putExtras(bundle)
//      startActivity(intent)
//    }

    val photo = photoDAO.findById(user?.photo_id) ?: return

    val file = File(photo.path)
    if (file.exists()) {
      val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
      imagePerfil.setImageBitmap(myBitmap)
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

    val photo = photoDAO.findById(user?.photo_id) ?: return

    val file = File(photo.path)
    if (file.exists()) {
      val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
      val photoView = headerView?.findViewById<ImageView>(R.id.imagePerfil)
      photoView?.setImageBitmap(myBitmap)
    }
  }
}