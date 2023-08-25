package com.example.folio.features.user.repository

import com.example.folio.features.user.model.User
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
): UserRepository {
    override suspend fun retrieveUserByUsername(username: String): User {
        return userService.retrieveUser(username).toModel()
    }

    override suspend fun retrieveUsers(ids: Set<String>): List<User> {
        return supervisorScope {
             ids.map { id -> async { retrieveUserByUsername(id) } }.awaitAll()
        }
    }
}
