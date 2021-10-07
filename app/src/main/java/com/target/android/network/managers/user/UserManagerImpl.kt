package com.target.android.network.managers.user

import com.target.android.network.models.User
import com.target.android.network.models.UserSerializer
import com.target.android.network.services.ApiService
import com.target.android.util.extensions.ActionCallback
import com.target.android.util.extensions.Data
import javax.inject.Inject

/**
 * Singleton class
 * */
class UserManagerImpl @Inject constructor(private val service: ApiService) : UserManager {

    override suspend fun signUp(user: User): Result<Data<UserSerializer>> =
        ActionCallback.call(service.signUp(UserSerializer(user)))

    override suspend fun signIn(user: User): Result<Data<UserSerializer>> =
        ActionCallback.call(service.signIn(UserSerializer(user)))

    override suspend fun signOut(): Result<Data<Void>> =
        ActionCallback.call(service.signOut())
}
