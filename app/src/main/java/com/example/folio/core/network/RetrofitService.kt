package com.example.folio.core.network

import com.example.folio.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitService {

    @Provides
    @Singleton
    fun provide(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.flickr.com/services/rest?format=json&nojsoncallback=1&api_key=${BuildConfig.FLICKR_API_KEY}")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}