package com.mobile.bookinder.screens.feed

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.interfaces.FeedCardBookEvent
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.databinding.FragmentFeedBinding
import com.mobile.bookinder.screens.my_books.BookActivity
import java.util.*

class FeedFragment: Fragment(), FeedCardBookEvent {
  private var _binding: FragmentFeedBinding? = null
  private val binding get() = _binding!!

  private lateinit var itemList: RecyclerView
  private var adapter: FirestoreRecyclerAdapter<Book, BookAdapter.MessageViewHolder>? = null

  private var storage = Firebase.storage
  private var db = Firebase.firestore
  private var auth = Firebase.auth

  private var storageRef = storage.reference

  private lateinit var fusedLocationClient: FusedLocationProviderClient

  @RequiresApi(Build.VERSION_CODES.N)
  val locationPermissionRequest = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    when {
      permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
        // Precise location access granted.
      }
      permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
        // Only approximate location access granted.
      }
      else -> {
        // No location access granted.
      }
    }
  }

  @SuppressLint("MissingPermission")
  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    val query: Query = FirebaseFirestore.getInstance()
      .collection("books")
      .whereNotEqualTo("owner", auth.currentUser?.uid)

    val options: FirestoreRecyclerOptions<Book> = FirestoreRecyclerOptions.Builder<Book>()
      .setQuery(query, Book::class.java)
      .build()

    adapter = BookAdapter(this, options)

    _binding = FragmentFeedBinding.inflate(inflater, container, false)

    setUpRecyclerView(binding.root)

    if (ContextCompat.checkSelfPermission(binding.root.context,
        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
      || ContextCompat.checkSelfPermission(binding.root.context,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
    ) {
      locationPermissionRequest.launch(arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(binding.root.context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
      if (location != null) {
        db.collection("users")
          .document(auth.currentUser?.uid.toString())
          .get()
          .addOnSuccessListener {
            val ownerName = "${it.data?.get("firstname")} ${it.data?.get("lastname")}"

            val data = hashMapOf(
              "latitude" to location.latitude,
              "longitude" to location.longitude,
              "owner" to auth.currentUser?.uid.toString(),
              "owner_name" to ownerName
            )

            db.collection("locations")
              .document(auth.currentUser?.uid.toString())
              .set(data)
          }
      }
    }

    return binding.root;
  }

  private fun setUpRecyclerView(view: View) {
    itemList = view.findViewById(R.id.itemListMyFeed)
    itemList.layoutManager = LinearLayoutManager(view.context)
    itemList.adapter = adapter
  }

  private fun actionLikeBook(context: Context, book: Book, pos: Int) {
    Toast.makeText(context, "Curtiu ${book.title}!", Toast.LENGTH_SHORT).show()
  }

  private fun openBookPage(book: Book){
    val bundle = Bundle()
    bundle.putSerializable("book", book)

    val intent = Intent(this.context, BookActivity::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  override fun onStart() {
    super.onStart()
    adapter?.startListening()
  }

  override fun onResume() {
    super.onResume()
    adapter?.notifyDataSetChanged()
  }

  override fun onDestroy() {
    super.onDestroy()
    adapter?.stopListening()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun showCardBook(book: Book, position: Int) {
    openBookPage(book)
  }

  override fun likeBook(book: Book, position: Int) {
      val like = Like(UUID.randomUUID().toString(),
        auth.currentUser?.uid.toString(),
        book.owner.toString(),
        book.book_id.toString())

    like.like_id?.let { id ->
      db.collection("likes").document(id).set(like).addOnCompleteListener { task ->
        if (task.isSuccessful){
          adapter?.notifyItemChanged(position)

          Toast.makeText(context, "Você curtiu ${book.title}", Toast.LENGTH_SHORT).show()
        }else{
          Toast.makeText(context, "Erro ao curtir livro", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  override fun deslikeBook(book: Book, position: Int, like: Like?) {
    db.collection("likes")
      .whereEqualTo("user_id_from", auth.currentUser?.uid)
      .whereEqualTo("book_id_to", book.book_id)
      .get()
      .addOnCompleteListener { task ->
        if (task.isSuccessful){
          for(document in task.result.documents)
            document.reference.delete()

          adapter?.notifyItemChanged(position)
          Toast.makeText(context, "Você descurtiu ${book.title}", Toast.LENGTH_SHORT).show()
        }else{
          Toast.makeText(context, "Erro ao descurtir livro", Toast.LENGTH_SHORT).show()
        }
      }
  }
}