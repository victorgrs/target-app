package com.target.android.ui.activity.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.target.android.network.managers.session.SessionManager
import com.target.android.network.managers.user.UserManager
import com.target.android.network.models.User
import com.target.android.ui.base.BaseViewModel
import com.target.android.util.NetworkState
import com.target.android.util.extensions.ApiErrorType
import com.target.android.util.extensions.ApiException
import kotlinx.coroutines.launch

open class SignUpActivityViewModel @ViewModelInject constructor(
    private val sessionManager: SessionManager,
    private val userManager: UserManager
) : BaseViewModel() {

    private val _state = MutableLiveData<SignUpState>()
    val state: LiveData<SignUpState>
        get() = _state

    fun signUp(user: User) {
        _networkState.value = NetworkState.loading
        viewModelScope.launch {
            val result = userManager.signUp(user = user)

            if (result.isSuccess) {
                result.getOrNull()?.value?.user?.let { user ->
                    sessionManager.signIn(user)
                }

                _networkState.value = NetworkState.idle
                _state.value = SignUpState.signUpSuccess
            } else {
                handleError(result.exceptionOrNull())
            }
        }
    }

    private fun handleError(exception: Throwable?) {
        error = if (exception is ApiException && exception.errorType == ApiErrorType.apiError) {
            exception.message
        } else null

        _networkState.value = NetworkState.idle
        _networkState.value = NetworkState.error
        _state.value = SignUpState.signUpFailure
    }
}

enum class SignUpState {
    signUpFailure,
    signUpSuccess
}
