package com.github.lalbuquerque.dogapp.repository

import com.github.lalbuquerque.dogapp.api.DogApi
import com.github.lalbuquerque.dogapp.db.dao.UserDao
import com.github.lalbuquerque.dogapp.db.entity.UserEntity
import com.github.lalbuquerque.dogapp.api.vo.User
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LoginRepository @Inject constructor(private val dogApi: DogApi,
                                          private val userDao: UserDao) {

    private var userEntity: UserEntity? = null

    var user: User? = null
        private set
        get() = User(userEntity?.email ?: "", userEntity?.token ?: "")

    val isLoggedIn: Boolean
        get() =  userEntity?.email != null && userEntity?.token != null

    init {
        userEntity = userDao.get()
    }

    fun login(email: String): Observable<User> {
        return Observable.defer {
            val userEntity: UserEntity? = userDao.get()
            if (userEntity?.token?.isNotEmpty() == true) {
                return@defer Observable.just(User(userEntity.email, userEntity.token))
            } else {
                return@defer dogApi.login(User(email = email))
                    .map { it.user }
                    .doOnNext {
                        val newUserEntity = UserEntity(it.email, it.token)
                        userDao.insert(newUserEntity)
                        user = it
                    }
            }
        }
    }
}