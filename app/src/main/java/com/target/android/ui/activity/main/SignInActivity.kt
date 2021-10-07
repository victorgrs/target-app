package com.target.android.ui.activity.main

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.target.android.R
import com.target.android.databinding.ActivitySignInBinding
import com.target.android.metrics.Analytics
import com.target.android.metrics.PageEvents
import com.target.android.metrics.VISIT_SIGN_IN
import com.target.android.network.models.User
import com.target.android.ui.view.AuthView
import com.target.android.util.NetworkState
import com.target.android.util.extensions.value
import com.target.android.util.permissions.PermissionActivity
import com.target.android.util.permissions.PermissionResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : PermissionActivity(), AuthView {

    private val viewModel: SignInActivityViewModel by viewModels()
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)
        Analytics.track(PageEvents.visit(VISIT_SIGN_IN))

        binding.signInButton.setOnClickListener { signIn() }

        lifecycle.addObserver(viewModel)

        setObservers()
        sampleAskForPermission()
    }

    override fun showProfile() {
        startActivityClearTask(ProfileActivity())
    }

    private fun signIn() {
        with(binding) {
            val user = User(
                email = emailEditText.value(),
                password = passwordEditText.value()
            )
            viewModel.signIn(user)
        }
    }

    private fun setObservers() {
        viewModel.state.observe(this, Observer {
            when (it) {
                SignInState.signInFailure -> showError(viewModel.error)
                SignInState.signInSuccess -> showProfile()
            }
        })

        viewModel.networkState.observe(this, Observer {
            when (it) {
                NetworkState.loading -> showProgress()
                NetworkState.idle -> hideProgress()
                else -> showError(viewModel.error ?: getString(R.string.default_error))
            }
        })
    }

    private fun sampleAskForPermission() {
        requestPermission(arrayOf(Manifest.permission.CAMERA), object : PermissionResponse {
            override fun granted() {
                // TODO..
            }

            override fun denied() {
                // TODO..
            }

            override fun foreverDenied() {
                // TODO..
            }
        })
    }
}
