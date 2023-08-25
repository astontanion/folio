package com.example.folio.features.user.repository

import com.example.folio.features.user.model.User
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface UserRepository {

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    interface Module {
        @Binds
        @Singleton
        fun bind(impl: UserRepositoryImpl): UserRepository
    }

    suspend fun retrieveUserByUsername(username: String): User
    suspend fun retrieveUsers(ids: Set<String>): List<User>
}