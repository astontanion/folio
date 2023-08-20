package com.example.folio.core.network

import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface NetworkMonitor {
    val isOnLine: Flow<Boolean>

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    interface Module {
        @Binds
        @Singleton
        fun bind(impl: NetworkMonitorImpl): NetworkMonitor
    }
}