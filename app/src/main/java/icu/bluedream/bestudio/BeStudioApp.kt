package icu.bluedream.bestudio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import icu.bluedream.bestudio.backend.CrashHandler

@HiltAndroidApp
class BeStudioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashHandler(this)
    }
}