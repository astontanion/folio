package com.example.folio.core.network

import com.example.folio.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class FlickrDefaultQueryInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("api_key", BuildConfig.FLICKR_API_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}