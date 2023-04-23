package com.example.mobileappsproject

import android.annotation.SuppressLint
import android.content.Intent
//import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.example.mobileappsproject.databinding.ActivityLoginBinding
import com.example.mobileappsproject.databinding.ActivityRegisterBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.functions.BiFunction
import io.reactivex.Observable



@SuppressLint("CheckResult")
class registerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fullname validation
        val nameStream= RxTextView.textChanges(binding.etfullName)
            .skipInitialValue()
            .map { name -> name.isEmpty() }
        nameStream.subscribe{ showNameExistAlert(it)}

        //email validation
        val emailStream = RxTextView.textChanges(binding.etEmailRegister)
            .skipInitialValue()
            .map { email ->  !Patterns.EMAIL_ADDRESS.matcher(email).matches()}
        emailStream.subscribe{showEmailValidAlert(it)}

        //username validation
        val usernameStream = RxTextView.textChanges(binding.etUsername)
            .skipInitialValue()
            .map { username -> username.length < 6 }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Username")
        }
        //password validation
        val passwordStream = RxTextView.textChanges(binding.etUsername)
            .skipInitialValue()
            .map { password -> password.length < 6 }
        passwordStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

        // confirm password validation
        val passwordConfirmStream = Observable.combineLatest(
            RxTextView.textChanges(binding.etPassword),
            RxTextView.textChanges(binding.etconfirmPassword),
            BiFunction { password: CharSequence, confirmPassword: CharSequence ->
                password.toString() != confirmPassword.toString()
            }
        )
        passwordConfirmStream.subscribe {
            showPasswordConfirmAlert(it)
        }
        //button enable true or false
        val invalidFieldsStream = Observable.combineLatest(
            nameStream,
            emailStream,
            usernameStream,
            passwordStream,
            passwordConfirmStream,
            {nameInvalid: Boolean, emailInvalid : Boolean, userNameInvalid : Boolean, passwordInvalid : Boolean, passwordConfirmInvalid : Boolean ->
            !nameInvalid && !emailInvalid && !userNameInvalid && !passwordInvalid && ! passwordConfirmInvalid
            })
        invalidFieldsStream.subscribe{isValid ->
            if (isValid){
                binding.appRegister.isEnabled = true
               binding.appRegister.backgroundTintList = ContextCompat.getColorStateList(this,
                    androidx.appcompat.R.color.primary_dark_material_light)
            }
            else{
                binding.appRegister.isEnabled = false
                binding.appRegister.backgroundTintList = ContextCompat.getColorStateList(this,
                    androidx.appcompat.R.color.primary_dark_material_light)
            }
        }
        //click

        binding.appRegister.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))

        }
        binding.haveAccount.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
    private fun showNameExistAlert(isNotValid: Boolean){
        binding.etfullName.error = if (isNotValid) "Username Does Not Exist!" else null
    }
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text=="Username")
        {
            binding.etUsername.error = if (isNotValid) "$text Must Be More Than Six Letters." else null
        }
        else if (text == "Password")
        {
            binding.etPassword.error = if (isNotValid) "$text Must Be More Than Eight Letters." else null

        }
    }
    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.etEmailRegister.error = if (isNotValid) "Email is not Valid." else null
    }
    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.etconfirmPassword.error = if (isNotValid) "Password is not Valid." else null
    }
}