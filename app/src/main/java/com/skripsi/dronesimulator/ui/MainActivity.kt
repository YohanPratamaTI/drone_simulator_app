package com.skripsi.dronesimulator.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.databinding.ActivityMainBinding
import com.skripsi.dronesimulator.ui.login.LoginActivity
import com.skripsi.dronesimulator.ui.status.StatusFragment
import com.skripsi.dronesimulator.ui.maps.MapsFragment
import com.skripsi.dronesimulator.ui.profile.ProfileFragment
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        val bottomNav = binding.bottomNavigation

        val fragment = StatusFragment.newInstance("test1", "test2")
        bottomNav.setOnItemSelectedListener(menuItemSelected)
        loadFragment(fragment)
    }

    private val menuItemSelected = NavigationBarView.OnItemSelectedListener  { item ->
        when (item.itemId) {
            R.id.menu_status ->{
                val fragment = StatusFragment.newInstance("test1", "test2")
                loadFragment(fragment)
                return@OnItemSelectedListener  true
            }
            R.id.menu_map ->{
                val fragment = MapsFragment.newInstance("test1", "test2")
                loadFragment(fragment)
                return@OnItemSelectedListener true
            }
            R.id.menu_profile ->{
                val fragment = ProfileFragment.newInstance("test1", "test2")
                loadFragment(fragment)
                return@OnItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_baru, fragment, fragment.javaClass.simpleName)
            .commit()
    }
    private fun setupViewModel() {
        mainViewModel= ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[MainViewModel::class.java]

        mainViewModel.getIsLogin().observe(this){
            if (!it) {
                // If the user is already logged in, navigate to MainActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

