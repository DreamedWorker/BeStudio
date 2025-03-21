package icu.bluedream.bestudio

import android.app.Application
import icu.bluedream.bestudio.backend.CrashHandler

class BeStudioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashHandler(this)
    }
}