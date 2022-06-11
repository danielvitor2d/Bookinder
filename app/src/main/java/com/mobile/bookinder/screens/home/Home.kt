package com.mobile.bookinder.screens.home

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobile.bookinder.R
import com.mobile.bookinder.databinding.HomeBinding
import com.mobile.bookinder.screens.feed.FeedFragment
import com.mobile.bookinder.screens.loans.LoansFragment
import com.mobile.bookinder.screens.my_books.MyBooksFragment
import com.mobile.bookinder.screens.profile.ProfileFragment
import com.mobile.bookinder.screens.settings.SettingsFragment

class Home: AppCompatActivity() {
  lateinit var drawerLayout: DrawerLayout
  private lateinit var navigationView: NavigationView
  private lateinit var binding: HomeBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = HomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
      FeedFragment()).commit()

    setSupportActionBar(binding.appbar.toolbar)

    drawerLayout = binding.drawerLayout
    navigationView = binding.navigationView

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

    val toggle = ActionBarDrawerToggle(this, drawerLayout, binding.appbar.toolbar, R.string.open_drawer, R.string.close_drawer)

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