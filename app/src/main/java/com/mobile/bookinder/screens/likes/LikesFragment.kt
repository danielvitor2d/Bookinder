package com.mobile.bookinder.screens.likes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mobile.bookinder.R
import com.mobile.bookinder.common.dao.BookDAO
import com.mobile.bookinder.common.dao.LikeDAO
import com.mobile.bookinder.common.model.Book
import com.mobile.bookinder.common.model.Like
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.databinding.FragmentLikesBinding
import com.mobile.bookinder.screens.feed.BookAdapter

class LikesFragment: Fragment() {
  private var _binding: FragmentLikesBinding? = null
  private val binding get() = _binding!!
  private var adapter: FirestoreRecyclerAdapter<Like, LikeAdapter.MessageViewHolder>? = null

  private var storage = Firebase.storage
  private var db = Firebase.firestore
  private var auth = Firebase.auth

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentLikesBinding.inflate(inflater, container, false)
    setUpRecyclerView(binding.root)
    return binding.root
  }

  private fun setUpRecyclerView(view: View) {
    val likesList = view.findViewById<RecyclerView>(R.id.matchesList)
    likesList.layoutManager = LinearLayoutManager(view.context)

    val query: Query = FirebaseFirestore.getInstance()
      .collection("likes")
      .whereEqualTo("user_id_to", auth.currentUser?.uid)

    val options: FirestoreRecyclerOptions<Like> = FirestoreRecyclerOptions.Builder<Like>()
      .setQuery(query, Like::class.java)
      .build()

    adapter = LikeAdapter(options)

    likesList.adapter = adapter
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

  override fun onDestroyView(){
    super.onDestroyView()
    _binding = null
  }
}