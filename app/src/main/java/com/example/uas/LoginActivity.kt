package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uas.databinding.ActivityLoginBinding
import com.example.uas.model.LoginRequest
import com.example.uas.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide() // Sembunyikan ActionBar untuk tampilan login yang bersih

        binding.buttonLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = binding.editTextUsername.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.buttonLogin.isEnabled = false // Nonaktifkan tombol saat loading

        lifecycleScope.launch {
            try {
                val request = LoginRequest(username, password)
                val response = RetrofitClient.instance.loginUser(request)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        Toast.makeText(this@LoginActivity, "Login Successful: ${it.username}", Toast.LENGTH_SHORT).show()
                        // Jika Anda ingin menyimpan token atau info user:
                        // val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        // with(sharedPref.edit()) {
                        //     putString("auth_token", it.token)
                        //     apply()
                        // }
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Tutup LoginActivity agar tidak bisa kembali dengan tombol back
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@LoginActivity, "Login Failed: ${errorBody ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.buttonLogin.isEnabled = true // Aktifkan kembali tombol
            }
        }
    }
}