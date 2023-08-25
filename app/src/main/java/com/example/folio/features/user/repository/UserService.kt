package com.example.folio.features.user.repository

import com.example.folio.features.user.model.UserWrapperDto
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

interface UserService {

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    object Module {
        @Provides
        @Singleton
        fun provide(retrofit: Retrofit): UserService {
            return retrofit.create(UserService::class.java)
        }
    }

    @GET("?method=flickr.people.findByUsername")
    suspend fun retrieveUser(
        @Query("username") userId: String
    ): UserWrapperDto
}