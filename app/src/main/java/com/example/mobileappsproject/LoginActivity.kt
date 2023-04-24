package com.example.mobileappsproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mobileappsproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlin.math.log


@SuppressLint("CheckResult")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //authentication
        auth = FirebaseAuth.getInstance()

        //username validation
        val usernameStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { username -> username.isEmpty() }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Username")
        }
        //password validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password -> password.isEmpty() }
        passwordStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }
        binding.appLogin.setOnClickListener{
            startActivity(Intent(this,homeActivity::class.java))
        }
        binding.noAccount.setOnClickListener{
            startActivity(Intent(this,registerActivity::class.java))
        }
        //button enable true or false
        val invalidFieldsStream = Observable.combineLatest(
            usernameStream,
            passwordStream,
            {userNameInvalid : Boolean, passwordInvalid : Boolean -> !userNameInvalid && !passwordInvalid})
        invalidFieldsStream.subscribe{isValid ->
            if (isValid){
                binding.appLogin.isEnabled = true
                binding.appLogin.backgroundTintList = ContextCompat.getColorStateList(this,
                 androidx.appcompat.R.color.primary_dark_material_light)
            }
            else{
                binding.appLogin.isEnabled = false
                binding.appLogin.backgroundTintList = ContextCompat.getColorStateList(this,
                    androidx.appcompat.R.color.primary_dark_material_light)
            }
        }
        binding.appLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            loginUser(email, password)

        }
        binding.noAccount.setOnClickListener{
            startActivity(Intent(this,registerActivity::class.java))
        }
    }
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text=="EMail/Username")
        {
            binding.etEmail.error = if (isNotValid) "$text Must Be More Than Six Letters." else null
        }
        else if (text == "Password")
        {
            binding.etPassword.error = if (isNotValid) "$text Must Be More Than Eight Letters." else null

        }
    }
    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ login ->
                if (login.isSuccessful){
                    Intent(this,homeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, login.exception?.message, Toast.LENGTH_SHORT).show()

                }

            }
    }
}