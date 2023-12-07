package com.example.sosmeddicoding.data.service

import com.example.sosmeddicoding.data.model.ResponseAddNewStory
import com.example.sosmeddicoding.data.model.ResponseGetAllStory
import com.example.sosmeddicoding.data.model.ResponseLogin
import com.example.sosmeddicoding.data.model.ResponseRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister

    @GET("stories")
    suspend fun getAllStories( @Query("page") page: Int = 1,
                               @Query("size") size: Int = 20): ResponseGetAllStory

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): ResponseGetAllStory

    @Multipart
    @POST("stories")
    suspend fun postStory(@Part file: MultipartBody.Part,
                          @Part("description") description: RequestBody,
                          @Part("lat") lat: RequestBody?,
                          @Part("lon") lon: RequestBody?
    ): ResponseAddNewStory
}