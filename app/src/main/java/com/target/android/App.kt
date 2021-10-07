package com.target.android

import android.app.Application
import com.target.android.metrics.Analytics
import com.target.android.metrics.GoogleAnalytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Analytics.addProvider(GoogleAnalytics(this))
        // You need the api key in order to use MixPanel
        // Analytics.addProvider(MixPanelAnalytics(this))
    }
}
