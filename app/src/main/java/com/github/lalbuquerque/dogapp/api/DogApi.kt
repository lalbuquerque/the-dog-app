package com.github.lalbuquerque.dogapp.api

import com.github.lalbuquerque.dogapp.api.vo.DogFeedResponse
import com.github.lalbuquerque.dogapp.api.vo.LoginResponse
import com.github.lalbuquerque.dogapp.api.vo.User
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface DogApi {

    @POST("signup")
    fun login(@Body user: User): Observable<LoginResponse>

    @GET("feed")
    fun getDogFeed(@Header("Authorization") token: String,
                   @Query("category") category: String): Observable<DogFeedResponse>
}