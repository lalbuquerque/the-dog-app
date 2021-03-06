package com.github.lalbuquerque.dogapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.github.lalbuquerque.dogapp.DogApplication
import com.github.lalbuquerque.dogapp.R
import com.github.lalbuquerque.dogapp.extensions.afterTextChanged
import com.github.lalbuquerque.dogapp.ui.dogfeed.DogFeedActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModel: LoginViewModel

    var didLogout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        didLogout = intent.getBooleanExtra(EXTRA_LOGOUT, false)

        DogApplication.component?.inject(this)
    }

    override fun onStart() {
        super.onStart()

        setUpLiveDataObservers()
        setUpLoginFormBehavior()
    }

    private fun setUpLiveDataObservers() {
        loginViewModel.loginStatusLiveData.observe(this@LoginActivity, Observer {
            if (didLogout) {
                didLogout = false
                loginViewModel.logout()
                return@Observer
            }

            if (it.loggedIn) {
                goToDogsActivity()
            }
        })

        loginViewModel.loginFormLiveData.observe(this@LoginActivity, Observer {

            login.isEnabled = it.isDataValid

            if (it.emailError != null) email.error = getString(it.emailError)
        })

        loginViewModel.loginResultLiveData.observe(this@LoginActivity, Observer {
            loading.visibility = View.GONE

            if (it.success) {
                goToDogsActivity()
            } else {
                showLoginFailed(it.error ?: R.string.generic_error)
            }
        })
    }

    private fun setUpLoginFormBehavior() {
        email.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(it)
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        loading.visibility = View.VISIBLE
                        loginViewModel.login(email.text.toString())
                    }
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(email.text.toString())
            }
        }
    }

    @Synchronized
    private fun goToDogsActivity() {
        startActivity(Intent(this, DogFeedActivity::class.java))
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        email.error = getString(errorString)
    }

    companion object { const val EXTRA_LOGOUT = "logout" }
}
