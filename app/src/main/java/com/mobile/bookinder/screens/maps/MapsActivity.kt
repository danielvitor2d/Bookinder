package com.mobile.bookinder.screens.maps

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
  private lateinit var mMap: GoogleMap
  private lateinit var binding: ActivityMapsBinding

  private lateinit var fusedLocationClient: FusedLocationProviderClient

  private val db = Firebase.firestore
  private val auth = Firebase.auth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMapsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    val mapFragment = supportFragmentManager
      .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
  }

  @SuppressLint("MissingPermission")
  override fun onMapReady(googleMap: GoogleMap) {
//    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//      Log.d("Location: ", "$location")
//      if (location == null) return@addOnSuccessListener
    mMap = googleMap

    // Add a marker in Sydney and move the camera
    // val sydney = LatLng(-34.0, 151.0)

    db.collection("locations")
      .document(auth.currentUser?.uid.toString())
      .get()
      .addOnSuccessListener {
        if (it.exists()) {
          val latitude = it.data?.get("latitude") as Double
          val longitude = it.data?.get("longitude") as Double
          val myLocation = LatLng(latitude, longitude)
          mMap.addMarker(MarkerOptions().position(myLocation).title("Eu"))
          mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 30F))
        }
      }

    db.collection("locations")
      .whereNotEqualTo("owner", auth.currentUser?.uid.toString())
      .get()
      .addOnSuccessListener {
        for (document in it.documents) {
          val latitude = document.data?.get("latitude") as Double
          val longitude = document.data?.get("longitude") as Double
          val myLocation = LatLng(latitude, longitude)
          mMap.addMarker(MarkerOptions().position(myLocation)
            .title(document.data?.get("owner_name") as String)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
        }
      }

//      val myLocation = LatLng(location.latitude, location.longitude)
//      Toast.makeText(this, "$myLocation", Toast.LENGTH_LONG).show()
//      Log.d("Location: ", "$myLocation")
//      mMap.addMarker(MarkerOptions().position(myLocation).title("Daniel"))
//      mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 30F))
//    }
  }
}