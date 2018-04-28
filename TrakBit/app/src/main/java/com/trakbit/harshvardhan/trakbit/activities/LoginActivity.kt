package com.trakbit.harshvardhan.trakbit.activities


import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import com.trakbit.harshvardhan.trakbit.R
import com.trakbit.harshvardhan.trakbit.ui.Constants
import io.realm.ObjectServerError
import io.realm.Realm
import io.realm.SyncCredentials
import io.realm.SyncUser

import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Realm.init(this)
        setContentView(R.layout.activity_login)
        email_sign_in_button.setOnClickListener {
            attemptLogin()
        }
        val users = SyncUser.all()
        if(users.isNotEmpty()) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun attemptLogin() {
        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        if (cancel) {
            focusView?.requestFocus()
        } else {
            // Realm Authentication
            val constants = Constants()
            val credentials = SyncCredentials.usernamePassword(emailStr, passwordStr)
            SyncUser.logInAsync(credentials, constants.AUTH_URL, object : SyncUser.Callback<SyncUser> {
                override fun onSuccess(user: SyncUser) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                override fun onError(error: ObjectServerError) {
                    println(error)
                }
            })
        }
    }

}
