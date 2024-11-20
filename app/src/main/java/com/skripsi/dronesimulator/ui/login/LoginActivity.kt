
package com.skripsi.dronesimulator.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.skripsi.dronesimulator.databinding.ActivityLoginBinding
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory
import com.skripsi.dronesimulator.model.Result
import com.skripsi.dronesimulator.ui.MainActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Long Range Surveillance App"

        setupViewModel()
        setupView()
    }

    private fun setupViewModel() {
        mainViewModel= ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[MainViewModel::class.java]

        mainViewModel.getIsLogin().observe(this){
            if (it) {
                // If the user is already logged in, navigate to MainActivity
                val intentToMain = Intent(this, MainActivity::class.java)
                startActivity(intentToMain)
                finish()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword

        binding.tvDemo.setOnClickListener {
            binding.etEmail.setText("demo@email.com")
            binding.etPassword.setText("demopassword")
        }

        binding.btMasuk.setOnClickListener {
            if ((etEmail.text.toString() == "demo@email.com") and (etPassword.text.toString() == "demopassword")){
                mainViewModel.loginDemo(email = etEmail.text.toString()).observe(this){
                    when(it) {
                        is Result.Success -> {
                            val intentToHome = Intent(this, MainActivity::class.java)
                            startActivity(intentToHome)
                            finish()

                            etEmail.text?.clear()
                            etPassword.text?.clear()
                        }
                        is Result.Error -> {
                            Toast.makeText(this, "Gagal Login", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Gagal Login", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
             }
            else Toast.makeText(this, "Harap isi email dan password dengan benar",Toast.LENGTH_SHORT).show()
        }

    }
}