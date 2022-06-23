package com.mobile.bookinder.screens.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobile.bookinder.R
import com.mobile.bookinder.common.model.LoggedUser
import com.mobile.bookinder.databinding.ActivityHomeBinding
import com.mobile.bookinder.screens.feed.FeedFragment
import com.mobile.bookinder.screens.likes.LikesFragment
import com.mobile.bookinder.screens.matches.MatchesFragment
import com.mobile.bookinder.screens.my_books.MyBooksFragment
import com.mobile.bookinder.screens.profile.ProfileFragment
import com.mobile.bookinder.screens.settings.SettingsFragment
import com.mobile.bookinder.screens.sign_in.SignInActivity
import java.util.logging.Logger

class HomeActivity: AppCompatActivity() {
  lateinit var drawerLayout: DrawerLayout
  private lateinit var navigationView: NavigationView
  private lateinit var binding: ActivityHomeBinding

  private val loggedUser = LoggedUser()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
      FeedFragment()).commit()

    setSupportActionBar(binding.appbar.toolbar)

    drawerLayout = binding.drawerLayout
    navigationView = binding.navigationView

    navigationView.setNavigationItemSelectedListener {
      when(it.itemId) {
        R.id.feed_menu -> {
          supportActionBar?.title = "Feed"
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            FeedFragment()).commit()
        }
        R.id.profile_menu -> {
          supportActionBar?.title = "Perfil"
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            ProfileFragment()).commit()
        }
        R.id.my_books_menu -> {
          supportActionBar?.title = "Meus livros"
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            MyBooksFragment()).commit()
        }
        R.id.matches_menu -> {
          supportActionBar?.title = "Matches"
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            MatchesFragment()).commit()
        }
        R.id.likes_menu -> {
          supportActionBar?.title = "Curtidas"
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            LikesFragment()).commit()
        }
        R.id.settings_item -> {
          supportActionBar?.title = "Configurações"
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            SettingsFragment()).commit()
        }
        R.id.logout_item -> {
          val intent = Intent(this, SignInActivity::class.java)
          startActivity(intent)
          finish()
        }
      }
      return@setNavigationItemSelectedListener true
    }

    val toggle = ActionBarDrawerToggle(this, drawerLayout, binding.appbar.toolbar, R.string.open_drawer, R.string.close_drawer)

    drawerLayout.addDrawerListener(toggle)

    toggle.syncState()

    updateHeader()

    val headerView = navigationView.getHeaderView(0)
    val imageViewCloseNavigationView = headerView.findViewById<ImageView>(R.id.imageViewCloseNavigationView)
    imageViewCloseNavigationView.setOnClickListener {
      drawerLayout.closeDrawer(GravityCompat.START)
    }
  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START)
    } else {
      val currentFragment =  supportFragmentManager.findFragmentById(R.id.fragment_container)
      if (currentFragment is FeedFragment) {
        super.onBackPressed()
      }else{
        supportActionBar?.title = "Feed"
        drawerLayout.closeDrawer(GravityCompat.START)
        supportFragmentManager.beginTransaction().replace(
          R.id.fragment_container,
          FeedFragment()
        ).commit()
      }
    }
  }

  fun updateHeader() {
    val user = loggedUser.getUser()

    val headerView = navigationView.getHeaderView(0)

    val textViewUserName = headerView.findViewById<TextView>(R.id.textViewUserName)
    "${user?.firstname} ${user?.lastname}".also {
      textViewUserName.text = it
    }

    val textViewUserEmail = headerView.findViewById<TextView>(R.id.textViewUserEmail)
    "${user?.email}".also {
      textViewUserEmail.text = it
    }
  }
}