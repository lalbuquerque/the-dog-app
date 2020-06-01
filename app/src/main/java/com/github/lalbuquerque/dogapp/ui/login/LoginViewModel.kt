package com.github.lalbuquerque.dogapp.ui.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lalbuquerque.dogapp.repository.LoginRepository
import com.github.lalbuquerque.dogapp.R
import com.github.lalbuquerque.dogapp.di.qualifiers.ObserveOn
import com.github.lalbuquerque.dogapp.di.qualifiers.SubscribeOn
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.subscribeBy
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository,
                                         @SubscribeOn private val subscribeOnScheduler: Scheduler,
                                         @ObserveOn private val observeOnScheduler: Scheduler
) : ViewModel() {

    private val _loginStatusLiveData = MutableLiveData<LoginStatus>()
    val loginStatusLiveData: LiveData<LoginStatus>
        get() = _loginStatusLiveData

    private val _loginFormLiveData = MutableLiveData<LoginFormState>()
    val loginFormLiveData: LiveData<LoginFormState>
        get() = _loginFormLiveData

    private val _loginResultLiveData = MutableLiveData<LoginResult>()
    val loginResultLiveData: LiveData<LoginResult>
        get() = _loginResultLiveData

    init {
        _loginStatusLiveData.value = LoginStatus(loginRepository.isLoggedIn)
    }

    fun loginDataChanged(email: String) {
        if (!isEmailValid(email)) {
            _loginFormLiveData.value =
                LoginFormState(emailError = R.string.invalid_email)
        } else {
            _loginFormLiveData.value =
                LoginFormState(
                    isDataValid = true
                )
        }
    }

    fun login(email: String) {
        loginRepository.login(email)
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribeBy(
                onNext = {
                    _loginResultLiveData.value = LoginResult(success = true)
                },
                onError = {
                    _loginResultLiveData.value =
                        LoginResult(error = getLoginErrorString(it))
                },
                onComplete = {}
            )
    }

    fun logout() {
        loginRepository.logout()
        _loginStatusLiveData.value = LoginStatus(loggedIn = false)
    }

    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    private fun getLoginErrorString(throwable: Throwable): Int {
        return when (throwable) {
            is IOException -> R.string.network_error
            is HttpException ->
                if (throwable.code() == 400) R.string.login_api_invalid_email else R.string.generic_error
            else -> R.string.generic_error
        }
    }
}