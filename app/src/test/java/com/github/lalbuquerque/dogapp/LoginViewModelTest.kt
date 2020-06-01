package com.github.lalbuquerque.dogapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.lalbuquerque.dogapp.api.vo.User
import com.github.lalbuquerque.dogapp.repository.LoginRepository
import com.github.lalbuquerque.dogapp.ui.login.LoginViewModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var loginRepositoryMock: LoginRepository = mock()
    var loginViewModel: LoginViewModel? = null

    @Before
    fun setUpViewModel() {
        loginViewModel =
            LoginViewModel(loginRepositoryMock, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    fun `given view called loginDataChanged() with invalid email then viewmodel should update loginFormLiveData isDataValid with false and emailError a valid value`() {
        loginViewModel!!.loginDataChanged("1234")

        assertEquals(false, loginViewModel!!.loginFormLiveData.value!!.isDataValid)
        assertNotNull(loginViewModel!!.loginFormLiveData.value!!.emailError)
    }

    @Test
    fun `given view called loginDataChanged() with valid email then viewmodel should update loginFormLiveData isDataValid with true and error null`() {
        loginViewModel!!.loginDataChanged("a@b.c")

        assertEquals(true, loginViewModel!!.loginFormLiveData.value!!.isDataValid)
        assertNull(loginViewModel!!.loginFormLiveData.value!!.emailError)
    }

    @Test
    fun `given view called login() then viewmodel should call repository login()`() {
        whenever(loginRepositoryMock.login(any()))
            .thenAnswer { Observable.just(User("")) }

        loginViewModel!!.login("")

        verify(loginRepositoryMock).login(any())
    }

    @Test
    fun `given view called login() with an email then viewmodel should call repository login() with same email`() {
        whenever(loginRepositoryMock.login(any()))
            .thenAnswer { Observable.just(User("")) }

        val email = "a@b.com"

        loginViewModel!!.login(email)

        val emailArgumentCaptor = argumentCaptor<String>()
        verify(loginRepositoryMock).login(emailArgumentCaptor.capture())

        assertEquals(email, emailArgumentCaptor.firstValue)
    }

    @Test
    fun `given repository emits error on login() then loginResultLiveData should indicate error`() {
        whenever(loginRepositoryMock.login(any()))
            .thenAnswer { Observable.error<User>(Throwable("")) }

        loginViewModel!!.login("")

        assertEquals(false, loginViewModel!!.loginResultLiveData.value!!.success)
        assertNotNull(loginViewModel!!.loginResultLiveData.value!!.error)
    }

    @Test
    fun `given repository emits valid data on login() then loginResultLiveData should indicate success`() {
        whenever(loginRepositoryMock.login(any()))
            .thenAnswer { Observable.just(User("")) }

        loginViewModel!!.login("a@b.com")

        assertEquals(true, loginViewModel!!.loginResultLiveData.value!!.success)
        assertNull(loginViewModel!!.loginResultLiveData.value!!.error)
    }

    @Test
    fun `given respository isLoggedIn is true then loginStatusLiveData should indicate the same`() {
        whenever(loginRepositoryMock.isLoggedIn).thenAnswer { true }

        val loginViewModel = LoginViewModel(loginRepositoryMock, Schedulers.trampoline(), Schedulers.trampoline())

        assertEquals(true, loginViewModel.loginStatusLiveData.value!!.loggedIn)
    }
}