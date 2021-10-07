package com.target.android.ui.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.target.android.R
import com.target.android.metrics.Analytics
import com.target.android.metrics.PageEvents
import com.target.android.metrics.VISIT_PROFILE
import com.target.android.network.managers.session.SessionManager
import com.target.android.ui.base.BaseActivity
import com.target.android.ui.view.ProfileView
import com.target.android.util.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity(), ProfileView {

    @Inject lateinit var sessionManager: SessionManager

    private val viewModel: ProfileActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Analytics.track(PageEvents.visit(VISIT_PROFILE))

        welcome_text_view.text = getString(R.string.welcome_message, sessionManager.user?.firstName)
        sign_out_button.setOnClickListener { viewModel.signOut() }

        lifecycle.addObserver(viewModel)
        setObservers()
    }

    override fun goToFirstScreen() {
        startActivityClearTask(SignUpActivity())
    }

    private fun setObservers() {
        viewModel.state.observe(this, Observer {
            when (it) {
                ProfileState.signOutFailure -> showError(viewModel.error)
                ProfileState.signOutSuccess -> goToFirstScreen()
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
