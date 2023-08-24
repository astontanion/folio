package com.example.folio.features.photo.repository

import com.example.folio.features.photo.model.PhotoDetailWrapperDto
import com.example.folio.features.photo.model.PhotoListDto
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
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

    @GET("?method=flickr.interestingness.getList&extras=tags,owner_name,o_dims, views,path_alias,url_m")
    suspend fun retrievePhotos(): PhotoListDto

    @GET("?method=flickr.photos.search&safe_search=1&extras=tags,owner_name,o_dims, views,path_alias,url_m")
    suspend fun searchPhotos(
        @Query("tags") tags: String? = null,
        @Query("tag_mode") tagMode: String? = null
    ): PhotoListDto

    @GET("?method=flickr.people.getPhotos&extras=tags,owner_name,o_dims, views,path_alias,url_m")
    suspend fun retrieveUserPhotos(
        @Query("user_id") userId: String
    ): PhotoListDto

    @GET("?method=flickr.photos.getInfo")
    suspend fun retrievePhotoWithId(
        @Query("photo_id") photoId: String
    ): PhotoDetailWrapperDto
}