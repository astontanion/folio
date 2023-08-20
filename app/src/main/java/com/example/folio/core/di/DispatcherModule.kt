package com.example.folio.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Main

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Default

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Io

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Main
    @Provides
    fun mainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Default
    @Provides
    fun defaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Io
    @Provides
    fun ioDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}