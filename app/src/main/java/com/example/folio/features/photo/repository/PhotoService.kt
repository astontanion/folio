package com.example.folio.features.photo.repository

import com.example.folio.features.photo.model.PhotoListDto
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Singleton

interface PhotoService {
    @dagger.Module
    @InstallIn(SingletonComponent::class)
    object Module {
        @Provides
        @Singleton
        fun photoService(retrofit: Retrofit): PhotoService {
            return retrofit.create(PhotoService::class.java)
        }
    }

    @GET("?method=flickr.photos.getRecent")
    suspend fun getRecent(): PhotoListDto
}