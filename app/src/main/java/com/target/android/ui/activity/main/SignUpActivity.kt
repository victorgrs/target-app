package com.target.android.ui.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.target.android.R
import com.target.android.databinding.ActivitySignUpBinding
import com.target.android.metrics.Analytics
import com.target.android.metrics.PageEvents
import com.target.android.metrics.VISIT_SIGN_UP
import com.target.android.network.models.User
import com.target.android.ui.base.BaseActivity
import com.target.android.ui.view.AuthView
import com.target.android.util.NetworkState
import com.target.android.util.extensions.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity(), AuthView {

    private val viewModel: SignUpActivityViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)
        Analytics.track(PageEvents.visit(VISIT_SIGN_UP))

        with(binding) {
            signUpButton.setOnClickListener { signUp() }
            signInTextView.setOnClickListener { signIn() }
        }
        lifecycle.addObserver(viewModel)
        setObservers()
    }

    override fun showProfile() {
        startActivityClearTask(ProfileActivity())
    }

    private fun signIn() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun signUp() {
        with(binding) {
            val user = User(
                email = emailEditText.value(),
                firstName = firstNameEditText.value(),
                lastName = lastNameEditText.value(),
                password = passwordEditText.value()
            )
            viewModel.signUp(user)
        }
    }

    private fun setObservers() {
        viewModel.state.observe(this, Observer {
            when (it) {
                SignUpState.signUpFailure -> showError(viewModel.error)
                SignUpState.signUpSuccess -> showProfile()
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
}
