package com.mobile.bookinder.screens.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobile.bookinder.R
import com.mobile.bookinder.screens.feed.FeedFragment
import com.mobile.bookinder.screens.loans.LoansFragment
import com.mobile.bookinder.screens.my_books.MyBooksFragment
import com.mobile.bookinder.screens.profile.ProfileFragment
import com.mobile.bookinder.screens.settings.SettingsFragment
import kotlin.system.exitProcess

class Home: AppCompatActivity() {
  lateinit var drawerLayout: DrawerLayout
  lateinit var navigationView: NavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.home)
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
      FeedFragment()).commit()

    val toolbar: Toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)

    drawerLayout = findViewById(R.id.drawer_layout)
    navigationView = findViewById(R.id.navigation_view)

    navigationView.setNavigationItemSelectedListener {
      when(it.itemId) {
        R.id.feed_item -> {
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            FeedFragment()).commit()
        }
        R.id.profile_item -> {
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            ProfileFragment()).commit()
        }
        R.id.my_books_item -> {
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            MyBooksFragment()).commit()
        }
        R.id.loans_item -> {
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            LoansFragment()).commit()
        }
        R.id.settings_item -> {
          drawerLayout.closeDrawer(GravityCompat.START)
          supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            SettingsFragment()).commit()
        }
        R.id.logout_item -> {
          finish()
        }
      }
      return@setNavigationItemSelectedListener true
    }

    val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)

    drawerLayout.addDrawerListener(toggle)

    toggle.syncState()

  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }
}