package com.github.lalbuquerque.dogapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.lalbuquerque.dogapp.api.DogApi
import com.github.lalbuquerque.dogapp.api.vo.LoginResponse
import com.github.lalbuquerque.dogapp.api.vo.User
import com.github.lalbuquerque.dogapp.db.dao.UserDao
import com.github.lalbuquerque.dogapp.db.entity.UserEntity
import com.github.lalbuquerque.dogapp.repository.LoginRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.Assert.*

class LoginRepositoryTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var dogApiMock: DogApi = mock()
    var userDaoMock: UserDao = mock()

    var loginRepository: LoginRepository? = null


    @Before
    fun setUpViewModel() {
        loginRepository = LoginRepository(dogApiMock, userDaoMock)
    }

    @Test
    fun `given viewmodel called login() then repository should call userDaoMock get()`() {
        whenever(userDaoMock.get()).thenAnswer { UserEntity("abc", "123") }

        val testObserver = loginRepository!!.login("abc")
            .test()

        verify(userDaoMock, atLeastOnce()).get()

        testObserver.dispose()
    }

    @Test
    fun `given viewmodel called login() then repository should call dogApi login()`() {
        whenever(userDaoMock.get()).thenAnswer { UserEntity("") }

        whenever(dogApiMock.login(any()))
            .thenAnswer { Observable.just(LoginResponse(User(""))) }

        val testObserver = loginRepository!!.login("")
            .test()

        verify(dogApiMock).login(any())

        testObserver.dispose()
    }

    @Test
    fun `given viewmodel called login() with an email then repository should call dogApi get() with same user email`() {
        whenever(userDaoMock.get()).thenAnswer { UserEntity("") }

        whenever(dogApiMock.login(any()))
            .thenAnswer { Observable.just(LoginResponse(User(""))) }

        val email = "a@b.com"

        val testObserver = loginRepository!!.login(email)
            .test()

        val userArgumentCaptor = argumentCaptor<User>()
        verify(dogApiMock).login(userArgumentCaptor.capture())

        assertEquals(email, userArgumentCaptor.firstValue.email)

        testObserver.dispose()
    }

    @Test
    fun `given login() was successful and viewmodel checks isLoggedIn then it should be true`() {
        whenever(userDaoMock.get()).thenAnswer { UserEntity("") }

        whenever(dogApiMock.login(any()))
            .thenAnswer { Observable.just(LoginResponse(User("abc", "123"))) }

        val testObserver = loginRepository!!.login("")
            .test()

        assertEquals(true, loginRepository!!.isLoggedIn)

        testObserver.dispose()
    }

    @Test
    fun `given viewmodel called logout() then repository should call userDaoMock delete()`() {
        loginRepository!!.logout()

        verify(userDaoMock).delete()
    }
}