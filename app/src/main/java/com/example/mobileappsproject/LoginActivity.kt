package com.example.mobileappsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileappsproject.databinding.ActivityLoginBinding
import com.example.mobileappsproject.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.appLogin.setOnClickListener{
            startActivity(Intent(this,homeActivity::class.java))
        }
        binding.noAccount.setOnClickListener{
            startActivity(Intent(this,registerActivity::class.java))
        }
    }
}