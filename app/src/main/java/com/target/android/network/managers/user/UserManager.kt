package com.target.android.network.managers.user

import com.target.android.network.models.User
import com.target.android.network.models.UserSerializer
import com.target.android.util.extensions.Data

interface UserManager {
    suspend fun signUp(user: User): Result<Data<UserSerializer>>
    suspend fun signIn(user: User): Result<Data<UserSerializer>>
    suspend fun signOut(): Result<Data<Void>>
}
