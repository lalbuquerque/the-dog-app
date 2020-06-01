package com.github.lalbuquerque.dogapp.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lalbuquerque.dogapp.repository.LoginRepository
import com.github.lalbuquerque.dogapp.R
import com.github.lalbuquerque.dogapp.di.qualifiers.ObserveOn
import com.github.lalbuquerque.dogapp.di.qualifiers.SubscribeOn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository,
                                         @SubscribeOn private val subscribeOnScheduler: Scheduler,
                                         @ObserveOn private val observeOnScheduler: Scheduler
) :
    ViewModel() {

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
                LoginFormState(emailError = R.string.invalid_username)
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
                    Log.i("viewmodel", it.toString())
                    _loginResultLiveData.value = LoginResult(success = true)
                },
                onError = {
                    _loginResultLiveData.value =
                        LoginResult(error = R.string.login_failed)
                },
                onComplete = {}
            )
    }

    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }
}