package com.target.android.network.managers

import com.target.android.network.managers.session.SessionManager
import com.target.android.network.managers.session.SessionManagerImpl
import com.target.android.network.managers.user.UserManager
import com.target.android.network.managers.user.UserManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindSessionManager(sessionManagerImpl: SessionManagerImpl): SessionManager

    @Binds
    @Singleton
    abstract fun bindUserManager(userManagerImplImpl: UserManagerImpl): UserManager
}
