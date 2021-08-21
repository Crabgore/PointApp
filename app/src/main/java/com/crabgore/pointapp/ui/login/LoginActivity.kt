package com.crabgore.pointapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crabgore.pointapp.ui.MainActivity
import com.crabgore.pointapp.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginResult
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var callbackManager: CallbackManager? = null
    private var tokenTracker: AccessTokenTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        checkToken()
    }

    private fun checkToken() {
        tokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                Timber.d("facebook $currentAccessToken")
                if (currentAccessToken != null) {
                    close()
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initUI() {
        callbackManager = CallbackManager.Factory.create()

        /**
         * добавлено для возможности смены пользователя, как того требует задание.
         * по умолчанию, в самлм facebook sdk невозможно этого реализовать (возможно выйти из
         * аккаунта, но при повторном входе будет предлагать только вход в предыдущий аккаунт,
         * без возможности смены)
         */
        binding.loginButton.loginBehavior = LoginBehavior.WEB_VIEW_ONLY

        binding.loginButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Timber.d("facebook success")
                close()
            }
            override fun onCancel() {}
            override fun onError(exception: FacebookException) {}
        })
    }

    private fun close() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}